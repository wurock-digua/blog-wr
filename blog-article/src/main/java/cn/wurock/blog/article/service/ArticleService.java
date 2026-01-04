package cn.wurock.blog.article.service;

import cn.wurock.blog.article.dto.ArticleDTO;
import cn.wurock.blog.article.dto.ArticlePageSelectDTO;
import cn.wurock.blog.article.dto.ArticleVO;
import cn.wurock.blog.common.result.PageResult;

public interface ArticleService {
	
	/**
	 * 新增文章
	 * @param articleDTO
	 */
	void addArticle(ArticleDTO articleDTO);
	
	/**
	 * 获取文章列表（分页查询）
	 * @param articlePageSelectDTO
	 * @return
	 */
	PageResult<ArticleVO> getArticleList(ArticlePageSelectDTO articlePageSelectDTO);
	
	/**
	 * 获取文章详情
	 * @param id
	 * @return
	 */
	ArticleVO getArticleDetail(Long id);
	
	/**
	 * 修改文章
	 * @param articleDTO
	 */
	void updateArticle(ArticleDTO articleDTO);
	
	/**
	 * 删除文章
	 * @param id
	 */
	void deleteArticle(Long id);
}
