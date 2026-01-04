package cn.wurock.blog.web.controller;

import cn.wurock.blog.auth.utils.SecurityContextUtil;
import cn.wurock.blog.common.result.Result;
import cn.wurock.blog.common.validation.UpdateGroup;
import cn.wurock.blog.user.dto.PasswordCheckDTO;
import cn.wurock.blog.user.dto.UserDTO;
import cn.wurock.blog.user.service.UserService;
import cn.wurock.blog.user.vo.UserVO;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 获取当前用户详细信息
	 * @return
	 */
	@GetMapping("/userInfo")
	public Result<UserVO> getCurrentUserInfo() {
		// 获取当前用户名
		String username = SecurityContextUtil.getCurrentUsername();
		UserVO userVO = userService.getCurrentUserInfo(username);
		return Result.success(userVO);
	}
	
	/**
	 * 修改当前用户信息(除密码和头像)
	 * @param userDTO
	 * @return
	 */
	@PutMapping("/updateUserInfo")
	public Result updateCurrentUserInfo(@Validated(UpdateGroup.class) @RequestBody UserDTO userDTO) {
		userService.updateCurrentUserInfo(userDTO);
		return Result.success();
	}
	
	/**
	 * 修改用户头像
	 * @param avatarUrl
	 * @return
	 */
	@PatchMapping("/updateAvatar")
	public Result updateAvatar(@URL @RequestParam("avatarUrl") String avatarUrl) {
		// 获取当前用户id
		Long userId = SecurityContextUtil.getCurrentUserId();
		userService.updateUserAvatar(avatarUrl,userId);
		return Result.success();
	}
	
	/**
	 * 修改用户密码
	 * @param passwordCheckDTO
	 * @return
	 */
	@PatchMapping("/updatePassword")
	public Result updatePassword(@Validated @RequestBody PasswordCheckDTO passwordCheckDTO) {
		// 获取当前用户id
		Long userId = SecurityContextUtil.getCurrentUserId();
		userService.updateUserPassword(passwordCheckDTO, userId);
		return Result.success();
	}
	
	/**
	 * 获取所有用户信息
	 * @return
	 */
	@GetMapping("/allUserInfo")
	public Result<List<UserVO>> getAllUserInfo() {
		List<UserVO> userVOList =userService.getAllUserInfo();
		return Result.success(userVOList);
	}
}
