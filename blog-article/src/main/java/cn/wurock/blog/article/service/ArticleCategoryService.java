package cn.wurock.blog.article.service;

import cn.wurock.blog.article.vo.ArticleCategoryVO;
import cn.wurock.blog.article.dto.ArticleCategoryDTO;

import java.util.List;

public interface ArticleCategoryService {
	/**
	 * 添加文章分类
	 * @param articleCategoryDTO
	 */
	void addArticleCategory(ArticleCategoryDTO articleCategoryDTO);
	
	/**
	 * 获取文章分类列表
	 * @return
	 */
	List<ArticleCategoryVO> getArticleCategoryList();
	
	/**
	 * 获取文章分类详情
	 * @param id
	 * @return
	 */
	ArticleCategoryVO getArticleCategoryById(Long id);
	
	/**
	 * 修改文章分类
	 * @param articleCategoryDTO
	 */
	void updateArticleCategory(ArticleCategoryDTO articleCategoryDTO);
	
	/**
	 * 删除文章分类
	 * @param id
	 */
	void deleteArticleCategory(Long id);
}
