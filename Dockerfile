# /Users/wurock/Public/coding/wurock-blog/blog-wr/Dockerfile
# springboot代码运行到基础镜像
FROM eclipse-temurin:21-jdk-jammy

# 工作目录
WORKDIR /app

# 将构建好的jar文件复制到容器中
#COPY blog-web/target/blog-web.jar app.jar
COPY blog-web/target/blog-web.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
