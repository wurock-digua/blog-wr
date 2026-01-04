package cn.wurock.blog.user.service.impl;

import cn.wurock.blog.common.exception.RegisterException;
import cn.wurock.blog.user.dto.PasswordCheckDTO;
import cn.wurock.blog.user.dto.UserDTO;
import cn.wurock.blog.user.entity.User;
import cn.wurock.blog.user.mapper.UserMapper;
import cn.wurock.blog.user.service.UserService;
import cn.wurock.blog.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 新用户注册
	 * @param userDTO
	 */
	@Override
	public void register(UserDTO userDTO) {
		// 查询数据库中是否存在该用户名
		User registerUser = userMapper.findUserByUsername(userDTO.getUsername());
		// 用户名已存在
		if (registerUser != null) {
			throw new RegisterException("用户名已存在");
		}
		// 查询数据库中是否存在该邮箱
		User registerEmailUser = userMapper.findUserByEmail(userDTO.getEmail());
		if (registerEmailUser != null) {
			throw new RegisterException("邮箱已存在");
		}
		// 创建新用户，加密密码
		String encodePwd = passwordEncoder.encode(userDTO.getPassword());
		User user = User.builder()
				.username(userDTO.getUsername())
				.password(encodePwd)
				.email(userDTO.getEmail())
				.build();
		user.prePersist(); // 设置创建时间和更新时间
		// 添加用户
		userMapper.addUser(user);
	}
	
	/**
	 * 获取当前用户详细信息
	 * @param username
	 * @return
	 */
	@Override
	public UserVO getCurrentUserInfo(String username) {
		User loginUser = userMapper.findUserByUsername(username);
		UserVO userVO = UserVO.builder()
				.id(loginUser.getId())
				.username(loginUser.getUsername())
				.nickname(loginUser.getNickname())
				.email(loginUser.getEmail())
				.userPic(loginUser.getUserPic())
				.createTime(loginUser.getCreateTime())
				.updateTime(loginUser.getUpdateTime())
				.build();
		return userVO;
	}
	
	/**
	 * 修改当前用户信息(除密码和头像)
	 * @param userDTO
	 */
	@Override
	public void updateCurrentUserInfo(UserDTO userDTO) {
		User user = User.builder()
				.id(userDTO.getId())
				.username(userDTO.getUsername())
				.nickname(userDTO.getNickname())
				.email(userDTO.getEmail())
				.build();
		user.preUpdate(); // 设置更新时间
		userMapper.updateUserById(user);
		
	}
	
	/**
	 * 修改当前用户头像
	 * @param avatarUrl
	 * @param id
	 */
	@Override
	public void updateUserAvatar(String avatarUrl, Long id) {
		User user = User.builder()
				.id(id)
				.userPic(avatarUrl)
				.build();
		user.preUpdate(); // 设置更新时间
		userMapper.updateUserPicById(user);
	}
	
	/**
	 * 修改用户密码
	 * @param passwordCheckDTO
	 * @param id
	 */
	@Override
	public void updateUserPassword(PasswordCheckDTO passwordCheckDTO, Long id) {
		// 判断输入的原密码是否正确
		User loginUser = userMapper.findUserById(id);
		if (!passwordEncoder.matches(passwordCheckDTO.getOldPassword(), loginUser.getPassword())) {
			throw new RegisterException("原密码错误");
		}
		// 获取新密码并加密
		String newPassword = passwordCheckDTO.getNewPassword();
		String encodePwd = passwordEncoder.encode(newPassword);
		
		User user = User.builder()
				.id(id)
				.password(encodePwd)
				.build();
		user.preUpdate();  // 设置更新时间
		userMapper.updateUserPasswordById(user);
	}
	
	/**
	 * 获取所有用户信息
	 * @return
	 */
	@Override
	public List<UserVO> getAllUserInfo() {
		List<User> userList = userMapper.getAllUser();
		List<UserVO> userVOList = userList.stream().map(user -> UserVO.builder()
				.id(user.getId())
				.username(user.getUsername())
				.nickname(user.getNickname())
				.email(user.getEmail())
				.userPic(user.getUserPic())
				.createTime(user.getCreateTime())
				.updateTime(user.getUpdateTime())
				.build()).toList();
		return userVOList;
	}
}
