package cn.wurock.blog.user.service;

import cn.wurock.blog.user.dto.PasswordCheckDTO;
import cn.wurock.blog.user.dto.UserDTO;
import cn.wurock.blog.user.vo.UserVO;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public interface UserService {
	
	/**
	 * 新用户注册
	 * @param userDTO
	 */
	void register(UserDTO userDTO);
	
	/**
	 * 获取当前用户详细信息
	 * @param username
	 * @return
	 */
	UserVO getCurrentUserInfo(String username);
	
	/**
	 * 修改当前用户信息(除密码和头像)
	 * @param userDTO
	 */
	void updateCurrentUserInfo(UserDTO userDTO);
	
	/**
	 * 修改当前用户头像
	 * @param avatarUrl
	 * @param id
	 */
	void updateUserAvatar(@URL String avatarUrl, Long id);
	
	/**
	 * 修改用户密码
	 * @param passwordCheckDTO
	 * @param id
	 */
	void updateUserPassword(PasswordCheckDTO passwordCheckDTO, Long id);
	
	/**
	 * 获取所有用户信息
	 * @return
	 */
	List<UserVO> getAllUserInfo();
}
