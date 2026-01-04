package cn.wurock.blog.article.mapper;

import cn.wurock.blog.article.entity.ArticleCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleCategoryMapper {
	
	/**
	 * 添加文章分类
	 * @param articleCategory
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into article_category(category_name, category_alias, create_user, create_time, update_time) " +
			"values(#{categoryName}, #{categoryAlias}, #{createUser}, #{createTime}, #{updateTime})")
	void addArticleCategory(ArticleCategory articleCategory);
	
	/**
	 * 获取文章分类列表
	 * @return
	 */
	@Select("select * from article_category")
	List<ArticleCategory> getArticleCategoryList();
	
	/**
	 * 根据id获取文章分类详情
	 * @param id
	 * @return
	 */
	@Select("select * from article_category where id = #{id}")
	ArticleCategory getArticleCategoryById(Long id);
	
	/**
	 * 修改文章分类
	 * @param articleCategory
	 */
	@Update("update article_category set category_name = #{categoryName}, category_alias = #{categoryAlias}, " +
			"update_time = #{updateTime} where id = #{id}")
	void updateArticleCategory(ArticleCategory articleCategory);
	
	/**
	 * 删除文章分类
	 * @param id
	 */
	@Delete("delete from article_category where id = #{id}")
	void deleteArticleCategory(Long id);
}
