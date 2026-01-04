package cn.wurock.blog.article.mapper;

import cn.wurock.blog.article.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
	
	/**
	 * 新增文章
	 * @param article
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time) " +
			"values(#{title}, #{content}, #{coverImg}, #{state}, #{categoryId}, #{createUser}, #{createTime}, #{updateTime})")
	void addArticle(Article article);
	
	/**
	 * 获取文章列表（条件查询）
	 * @param categoryId 分类ID
	 * @param state 发布状态
	 * @return
	 */
	List<Article> getArticleListByCondition(@Param("categoryId") Long categoryId,
											@Param("state") String state,
											@Param("createUser") Long createUser);
	
	/**
	 * 根据id获取文章详情
	 * @param id
	 * @return
	 */
	@Select("select * from article where id = #{id}")
	Article getArticleById(Long id);
	
	/**
	 * 修改文章
	 * @param article
	 */
	@Update("update article set title = #{title}, content = #{content}, cover_img = #{coverImg}, " +
			"state = #{state}, category_id = #{categoryId}, update_time = #{updateTime} where id = #{id}")
	void updateArticle(Article article);
	
	/**
	 * 根据id删除文章
	 * @param id
	 */
	@Delete("delete from article where id = #{id}")
	void deleteArticleById(Long id);
}
