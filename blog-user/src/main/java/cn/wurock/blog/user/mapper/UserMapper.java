package cn.wurock.blog.user.mapper;

import cn.wurock.blog.user.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	@Select("select * from user where username = #{username}")
	User findUserByUsername(String username);
	
	/**
	 * 根据邮箱查询用户
	 * @param email
	 * @return
	 */
	@Select("select * from user where email = #{email}")
	User findUserByEmail(String email);
	
	/**
	 * 根据用户ID查询用户信息
	 * @param id
	 * @return
	 */
	@Select("select * from user where id = #{id}")
	User findUserById(Long id);
	
	/**
	 * 添加用户
	 * @param user
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into user(username, password, email, create_time, update_time) " +
			"values(#{username}, #{password}, #{email}, #{createTime}, #{updateTime})")
	void addUser(User user);
	
	/**
	 * 根据用户ID更新用户信息
	 * @param user
	 */
	@Update("update user set username = #{username}, nickname = #{nickname}, email = #{email}, " +
			"update_time = #{updateTime} where id = #{id}")
	void updateUserById(User user);
	
	/**
	 * 根据用户ID更新用户头像
	 * @param user
	 */
	@Update("update user set user_pic = #{userPic}, update_time = #{updateTime} where id = #{id}")
	void updateUserPicById(User user);
	
	/**
	 * 根据用户ID更新用户密码
	 * @param user
	 */
	@Update("update user set password = #{password}, update_time = #{updateTime} where id = #{id}")
	void updateUserPasswordById(User user);
	
	/**
	 * 获取所有用户信息
	 * @return
	 */
	@Select("select * from user")
	List<User> getAllUser();
}
