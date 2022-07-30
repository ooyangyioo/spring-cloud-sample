package org.yangyi.project.oauth.dao;

import org.apache.ibatis.annotations.*;
import org.yangyi.project.oauth.entity.SysUser;
import org.yangyi.project.oauth.entity.SysUserRole;

@Mapper
public interface SysUserDao {

    @Delete("delete from sys_user where user_id = #{id}")
    int deleteById(Long id);

    @Insert("insert into sys_user(user_id, username, password, email, phone, create_time) values (#{userId}, #{username}, #{password}, #{email}, #{phone}, #{createTime})")
    int insert(SysUser record);

    /**
     * 根据用户主键查询用户基本信息
     *
     * @param id 用户主键
     * @return 用户
     */
    @Select("select * from sys_user where user_id = #{id}")
    SysUser selectById(Long id);

    /**
     * 根据用户名查询用户基本信息
     *
     * @param username 用户名
     * @return 用户
     */
    @Select("select * from sys_user where username = #{username}")
    SysUser selectByUsername(String username);

    /**
     * 查询用户及角色信息
     *
     * @param username 用户名
     * @return 用户及角色
     */
    SysUserRole selectUserAndRoleByUsername(@Param("username") String username);

    @Update({"update sys_user set  password = #{password}, email = #{email}, phone = #{phone}, enabled = #{enabled}, expired = #{expire}, locked = #{locked}, password_expired = #{passwordExpired} where id = #{id}"})
    int updateById(SysUser record);

}