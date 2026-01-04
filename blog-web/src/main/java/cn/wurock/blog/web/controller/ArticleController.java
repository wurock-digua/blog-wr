package cn.wurock.blog.web.controller;

import cn.wurock.blog.article.dto.ArticleDTO;
import cn.wurock.blog.article.dto.ArticlePageSelectDTO;
import cn.wurock.blog.article.dto.ArticleVO;
import cn.wurock.blog.article.service.ArticleService;
import cn.wurock.blog.auth.utils.SecurityContextUtil;
import cn.wurock.blog.common.result.PageResult;
import cn.wurock.blog.common.result.Result;
import cn.wurock.blog.common.validation.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
	/**
	 * 新增文章
	 * @param articleDTO
	 * @return
	 */
	@PostMapping
	public Result addArticle(@Validated @RequestBody ArticleDTO articleDTO) {
		// 获取当前用户id
		Long userId = SecurityContextUtil.getCurrentUserId();
		articleDTO.setCreateUser(userId);
		articleService.addArticle(articleDTO);
		return Result.success();
	}
	
	/**
	 * 获取文章列表（条件分页查询,全部）
	 * @param pageNum
	 * @param pageSize
	 * @param categoryId
	 * @param state
	 * @return
	 */
	@GetMapping("/list")
	public Result<PageResult<ArticleVO>> getArticleList(@RequestParam(defaultValue = "1") Integer pageNum,
														@RequestParam(defaultValue = "10") Integer pageSize,
														@RequestParam(required = false) Long categoryId,
														@RequestParam(required = false) String state) {
		ArticlePageSelectDTO articlePageSelectDTO = ArticlePageSelectDTO.builder()
				.pageNum(pageNum)
				.pageSize(pageSize)
				.categoryId(categoryId)
				.state(state)
				.build();
		PageResult<ArticleVO> articleList = articleService.getArticleList(articlePageSelectDTO);
		return Result.success(articleList);
	}
	
	/**
	 * 获取文章列表（条件分页查询,我的）
	 * @param pageNum
	 * @param pageSize
	 * @param categoryId
	 * @param state
	 * @return
	 */
	@GetMapping("/myList")
	public Result<PageResult<ArticleVO>> getArticleMyList(@RequestParam(defaultValue = "1") Integer pageNum,
														  @RequestParam(defaultValue = "10") Integer pageSize,
														  @RequestParam(required = false) Long categoryId,
														  @RequestParam(required = false) String state) {
		// 获取当前用户id
		Long userId = SecurityContextUtil.getCurrentUserId();
		ArticlePageSelectDTO articlePageSelectDTO = ArticlePageSelectDTO.builder()
				.pageNum(pageNum)
				.pageSize(pageSize)
				.categoryId(categoryId)
				.state(state)
				.createUser(userId)
				.build();
		PageResult<ArticleVO> articleList = articleService.getArticleList(articlePageSelectDTO);
		return Result.success(articleList);
	}
	
	/**
	 * 获取文章详情
	 * @param id
	 * @return
	 */
	@GetMapping("/detail")
	public Result<ArticleVO> getArticleDetail(@RequestParam Long id) {
		ArticleVO articleVO = articleService.getArticleDetail(id);
		return Result.success(articleVO);
	}
	
	/**
	 * 修改文章
	 * @param articleDTO
	 * @return
	 */
	@PutMapping
	public Result updateArticle(@Validated(UpdateGroup.class) @RequestBody ArticleDTO articleDTO) {
		articleService.updateArticle(articleDTO);
		return Result.success();
	}
	
	/**
	 * 删除文章
	 * @param id
	 * @return
	 */
	@DeleteMapping
	public Result deleteArticle(@RequestParam Long id) {
		articleService.deleteArticle(id);
		return Result.success();
	}
}
