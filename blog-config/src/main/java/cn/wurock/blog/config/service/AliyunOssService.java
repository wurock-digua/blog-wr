package cn.wurock.blog.config.service;

import cn.wurock.blog.common.exception.OssException;
import cn.wurock.blog.config.properties.AliyunOssProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Component
public class AliyunOssService {
    
    private static final Logger logger = LoggerFactory.getLogger(AliyunOssService.class);
    
    private final AliyunOssProperties ossProperties;
    private OSS ossClient;
    private String bucketName;
    
    public AliyunOssService(AliyunOssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }
    
    /**
     * 初始化OSS客户端
     */
    @PostConstruct
    public void init() {
        try {
            this.ossClient = new OSSClientBuilder().build(
                    ossProperties.getEndpoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret()
            );
            this.bucketName = ossProperties.getBucketName();
            logger.info("OSS Client initialized successfully for bucket: {}", bucketName);
        } catch (Exception e) {
            logger.error("Failed to initialize OSS Client", e);
            throw new OssException("OSS客户端初始化失败", e);
        }
    }
    
    /**
     * 销毁时关闭客户端
     */
    @PreDestroy
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
            logger.info("OSS Client closed");
        }
    }
    
    // ==================== 文件上传 ====================
    
    /**
     * 上传文件流
     * @param key 存储在OSS中的对象键 (如: images/2023/photo.jpg)
     * @param inputStream 文件输入流
     * @throws OssException 上传失败
     */
    public String upload(String key, InputStream inputStream) throws OssException {
        String url = "";
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream);
            ossClient.putObject(putObjectRequest);
            //url组成: https://bucket名称.区域节点/key
            String endpoint = ossProperties.getEndpoint();
            url = "https://"+bucketName+"."+endpoint.substring(endpoint.lastIndexOf("/")+1)+"/"+key;
            logger.info("File uploaded successfully: {}", key);
        } catch (Exception e) {
            logger.error("Upload failed for key: {}", key, e);
            throw new OssException("上传文件失败", e);
        }
        return url;
    }
    
    // ==================== 文件下载 ====================
    
    /**
     * 下载文件到输出流
     * @param key 对象键
     * @return ObjectMetadata 和 InputStream
     * @throws OssException
     */
    public DownloadResult download(String key) throws OssException {
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, key);
            return new DownloadResult(ossObject.getObjectMetadata(), ossObject.getObjectContent());
        } catch (Exception e) {
            logger.error("Download failed for key: {}", key, e);
            throw new OssException("文件下载失败",e);
        }
    }
    
    // ==================== 文件删除 ====================
    
    /**
     * 删除单个文件
     * @param key 对象键
     * @throws OssException
     */
    public void delete(String key) throws OssException {
        try {
            ossClient.deleteObject(bucketName, key);
            logger.info("File deleted: {}", key);
        } catch (Exception e) {
            logger.error("Delete failed for key: {}", key, e);
            throw new OssException("文件删除失败", e);
        }
    }
    
    /**
     * 批量删除文件
     * @param keys 对象键列表
     * @return DeleteObjectsResult
     * @throws OssException
     */
    public DeleteObjectsResult deleteBatch(List<String> keys) throws OssException {
        try {
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(keys);
            DeleteObjectsResult result = ossClient.deleteObjects(request);
            logger.info("Batch delete completed. Deleted {} objects.", keys.size());
            return result;
        } catch (Exception e) {
            logger.error("Batch delete failed", e);
            throw new OssException("文件批量删除失败", e);
        }
    }
    
    // ==================== 文件信息查询 ====================
    
    /**
     * 检查文件是否存在
     * @param key 对象键
     * @return true if exists
     * @throws OssException
     */
    public boolean exists(String key) throws OssException {
        try {
            return ossClient.doesObjectExist(bucketName, key);
        } catch (Exception e) {
            logger.error("Check existence failed for key: {}", key, e);
            throw new OssException("检查文件失败", e);
        }
    }
    
    /**
     * 获取文件元数据
     * @param key 对象键
     * @return ObjectMetadata
     * @throws OssException
     */
    public ObjectMetadata getObjectMetadata(String key) throws OssException {
        try {
            return ossClient.getObjectMetadata(bucketName, key);
        } catch (Exception e) {
            logger.error("Get metadata failed for key: {}", key, e);
            throw new OssException("获取文件与数据失败", e);
        }
    }
    
    // ==================== 签名URL ====================
    
    /**
     * 生成临时访问URL (默认1小时过期)
     * @param key 对象键
     * @return 可访问的URL
     * @throws OssException
     */
    public String generatePresignedUrl(String key) throws OssException {
        return generatePresignedUrl(key, 3600); // 1小时
    }
    
    /**
     * 生成临时访问URL
     * @param key 对象键
     * @param expireSeconds 过期时间(秒)
     * @return 可访问的URL
     * @throws OssException
     */
    public String generatePresignedUrl(String key, long expireSeconds) throws OssException {
        try {
            Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000);
            URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
            return url.toString();
        } catch (Exception e) {
            logger.error("Generate presigned URL failed for key: {}", key, e);
            throw new OssException("生成临时访问URL失败", e);
        }
    }
    
    // ==================== 内部辅助类 ====================
    
    /**
     * 下载结果封装
     */
    public static class DownloadResult implements AutoCloseable {
        public final ObjectMetadata metadata;
        public final InputStream inputStream;
        
        public DownloadResult(ObjectMetadata metadata, InputStream inputStream) {
            this.metadata = metadata;
            this.inputStream = inputStream;
        }
        
        @Override
        public void close() throws IOException {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}