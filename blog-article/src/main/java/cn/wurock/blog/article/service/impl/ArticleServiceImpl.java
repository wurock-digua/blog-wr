package cn.wurock.blog.article.service.impl;

import cn.wurock.blog.article.dto.ArticleDTO;
import cn.wurock.blog.article.dto.ArticlePageSelectDTO;
import cn.wurock.blog.article.dto.ArticleVO;
import cn.wurock.blog.article.entity.Article;
import cn.wurock.blog.article.mapper.ArticleMapper;
import cn.wurock.blog.article.service.ArticleService;
import cn.wurock.blog.common.result.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	/**
	 * 新增文章
	 * @param articleDTO
	 */
	@Override
	public void addArticle(ArticleDTO articleDTO) {
		Article article = Article.builder()
				.title(articleDTO.getTitle())
				.content(articleDTO.getContent())
				.coverImg(articleDTO.getCoverImg())
				.state(articleDTO.getState())
				.categoryId(articleDTO.getCategoryId())
				.createUser(articleDTO.getCreateUser())
				.build();
		article.prePersist();
		articleMapper.addArticle(article);
	}
	
	/**
	 * 获取文章列表（分页查询）
	 * @param articlePageSelectDTO
	 * @return
	 */
	@Override
	public PageResult<ArticleVO> getArticleList(ArticlePageSelectDTO articlePageSelectDTO) {
		// 获取查询参数
		Integer pageNum = articlePageSelectDTO.getPageNum();
		Integer pageSize = articlePageSelectDTO.getPageSize();
		Long categoryId = articlePageSelectDTO.getCategoryId();
		String state = articlePageSelectDTO.getState();
		Long createUser = articlePageSelectDTO.getCreateUser();
		
		// 开启分页查询
		PageHelper.startPage(pageNum, pageSize);
		List<Article> articleList = articleMapper.getArticleListByCondition(categoryId, state, createUser);
		
		// 使用PageInfo包装结果，它会自动计算分页信息（总页数、当前页等）
		PageInfo<Article> pageInfo = new PageInfo<>(articleList);
		// 获取总记录数
		long total = pageInfo.getTotal();
		// 获取当前页数据
		List<Article> list = pageInfo.getList();
		List<ArticleVO> articleVOList = list.stream().map(article -> ArticleVO.builder()
				.id(article.getId())
				.title(article.getTitle())
				.content(article.getContent())
				.coverImg(article.getCoverImg())
				.state(article.getState())
				.categoryId(article.getCategoryId())
				.createUser(article.getCreateUser())
				.createTime(article.getCreateTime())
				.updateTime(article.getUpdateTime())
				.build()).toList();
		
		PageResult<ArticleVO> pageResult = PageResult.<ArticleVO>builder()
				.total(total)
				.items(articleVOList)
				.build();
		
		return pageResult;
	}
	
	/**
	 * 获取文章详情
	 * @param id
	 * @return
	 */
	@Override
	public ArticleVO getArticleDetail(Long id) {
		Article article = articleMapper.getArticleById(id);
		ArticleVO articleVO = ArticleVO.builder()
				.id(article.getId())
				.title(article.getTitle())
				.content(article.getContent())
				.coverImg(article.getCoverImg())
				.state(article.getState())
				.categoryId(article.getCategoryId())
				.createUser(article.getCreateUser())
				.createTime(article.getCreateTime())
				.updateTime(article.getUpdateTime())
				.build();
		
		return articleVO;
	}
	
	/**
	 * 修改文章
	 * @param articleDTO
	 */
	@Override
	public void updateArticle(ArticleDTO articleDTO) {
		Article article = Article.builder()
				.id(articleDTO.getId())
				.title(articleDTO.getTitle())
				.content(articleDTO.getContent())
				.coverImg(articleDTO.getCoverImg())
				.state(articleDTO.getState())
				.categoryId(articleDTO.getCategoryId())
				.build();
		article.preUpdate();
		articleMapper.updateArticle(article);
	}
	
	/**
	 * 删除文章
	 * @param id
	 */
	@Override
	public void deleteArticle(Long id) {
		articleMapper.deleteArticleById(id);
	}
}
