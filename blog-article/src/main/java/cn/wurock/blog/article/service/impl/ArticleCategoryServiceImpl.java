package cn.wurock.blog.article.service.impl;

import cn.wurock.blog.article.vo.ArticleCategoryVO;
import cn.wurock.blog.article.dto.ArticleCategoryDTO;
import cn.wurock.blog.article.entity.ArticleCategory;
import cn.wurock.blog.article.mapper.ArticleCategoryMapper;
import cn.wurock.blog.article.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleCategoryServiceImpl implements ArticleCategoryService {
	
	@Autowired
	private ArticleCategoryMapper articleCategoryMapper;
	
	/**
	 * 添加文章分类
	 * @param articleCategoryDTO
	 */
	@Override
	public void addArticleCategory(ArticleCategoryDTO articleCategoryDTO) {
		ArticleCategory articleCategory = ArticleCategory.builder()
				.categoryName(articleCategoryDTO.getCategoryName())
				.categoryAlias(articleCategoryDTO.getCategoryAlias())
				.createUser(articleCategoryDTO.getCreateUser())
				.build();
		articleCategory.prePersist(); // 设置创建时间和更新时间
		articleCategoryMapper.addArticleCategory(articleCategory);
	}
	
	/**
	 * 获取文章分类列表
	 * @return
	 */
	@Override
	public List<ArticleCategoryVO> getArticleCategoryList() {
		ArrayList<ArticleCategoryVO> articleCategoryVOList = new ArrayList<>(); // 创建一个空的列表
		
		List<ArticleCategory> articleCategoryList = articleCategoryMapper.getArticleCategoryList();
		for (ArticleCategory articleCategory : articleCategoryList) {
			ArticleCategoryVO articleCategoryVO = ArticleCategoryVO.builder()
					.id(articleCategory.getId())
					.categoryName(articleCategory.getCategoryName())
					.categoryAlias(articleCategory.getCategoryAlias())
					.createTime(articleCategory.getCreateTime())
					.updateTime(articleCategory.getUpdateTime())
					.build();
			articleCategoryVOList.add(articleCategoryVO);
		}
		return articleCategoryVOList;
	}
	
	/**
	 * 根据id获取文章分类详情
	 * @param id
	 * @return
	 */
	@Override
	public ArticleCategoryVO getArticleCategoryById(Long id) {
		ArticleCategory articleCategory = articleCategoryMapper.getArticleCategoryById(id);
		ArticleCategoryVO articleCategoryVO = ArticleCategoryVO.builder()
				.id(articleCategory.getId())
				.categoryName(articleCategory.getCategoryName())
				.categoryAlias(articleCategory.getCategoryAlias())
				.createTime(articleCategory.getCreateTime())
				.updateTime(articleCategory.getUpdateTime())
				.build();
		return articleCategoryVO;
	}
	
	/**
	 * 修改文章分类
	 * @param articleCategoryDTO
	 */
	@Override
	public void updateArticleCategory(ArticleCategoryDTO articleCategoryDTO) {
		ArticleCategory articleCategory = ArticleCategory.builder()
				.id(articleCategoryDTO.getId())
				.categoryName(articleCategoryDTO.getCategoryName())
				.categoryAlias(articleCategoryDTO.getCategoryAlias())
				.build();
		articleCategory.preUpdate(); // 设置更新时间
		articleCategoryMapper.updateArticleCategory(articleCategory);
	}
	
	/**
	 * 删除文章分类
	 * @param id
	 */
	@Override
	public void deleteArticleCategory(Long id) {
		articleCategoryMapper.deleteArticleCategory(id);
		// 删除分类后，将该分类下的文章的分类id设置为null
	}
}
