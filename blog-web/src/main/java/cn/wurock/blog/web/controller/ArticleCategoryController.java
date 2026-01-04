package cn.wurock.blog.web.controller;

import cn.wurock.blog.article.vo.ArticleCategoryVO;
import cn.wurock.blog.article.dto.ArticleCategoryDTO;
import cn.wurock.blog.article.service.ArticleCategoryService;
import cn.wurock.blog.auth.utils.SecurityContextUtil;
import cn.wurock.blog.common.result.Result;
import cn.wurock.blog.common.validation.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article/category")
public class ArticleCategoryController{
	
	@Autowired
	private ArticleCategoryService articleCategoryService;
	
	/**
	 * 添加文章分类
	 * @param articleCategoryDTO
	 * @return
	 */
	@PostMapping
	public Result addArticleCategory(@Validated @RequestBody ArticleCategoryDTO articleCategoryDTO) {
		// 获取当前用户id
		Long userId = SecurityContextUtil.getCurrentUserId();
		articleCategoryDTO.setCreateUser(userId);
		articleCategoryService.addArticleCategory(articleCategoryDTO);
		return Result.success();
	}
	
	/**
	 * 获取文章分类列表
	 * @return
	 */
	@GetMapping
	public Result<List<ArticleCategoryVO>> getArticleCategoryList() {
		System.out.println("获取文章分类列表");
		List<ArticleCategoryVO> articleCategoryList = articleCategoryService.getArticleCategoryList();
		return Result.success(articleCategoryList);
	}
	
	/**
	 * 获取文章分类详情
	 * @param id
	 * @return
	 */
	@GetMapping("/detail")
	public Result<ArticleCategoryVO> getArticleCategoryById(@RequestParam Long id) {
		ArticleCategoryVO articleCategory = articleCategoryService.getArticleCategoryById(id);
		return Result.success(articleCategory);
	}
	
	/**
	 * 修改文章分类
	 * @param articleCategoryDTO
	 * @return
	 */
	@PutMapping
	public Result updateArticleCategory(@Validated(UpdateGroup.class) @RequestBody ArticleCategoryDTO articleCategoryDTO) {
		articleCategoryService.updateArticleCategory(articleCategoryDTO);
		return Result.success();
	}
	
	/**
	 * 删除文章分类
	 * @param id
	 * @return
	 */
	@DeleteMapping
	public Result deleteArticleCategory(@RequestParam Long id) {
		articleCategoryService.deleteArticleCategory(id);
		return Result.success();
	}
}
