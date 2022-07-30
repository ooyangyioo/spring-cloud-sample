package org.yangyi.project.oauth.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.oauth.entity.SysRole;

import java.util.List;

@Mapper
public interface SysRoleDao {

    SysRole selectById(Long roleId);

    @Delete({"delete from sys_role where role_id = #{id}"})
    int delete(Long id);

    int insert(SysRole record);

    /**
     * 查询全部的权限列表
     *
     * @return
     */
    @Select({"select * from sys_role"})
    List<SysRole> selectAll();

    /**
     * 查询用户的角色信息
     *
     * @param userId 用户主键
     * @return 用户所拥有的角色列表
     */
    List<SysRole> selectUserRole(@Param("userId") Long userId);

    int updateSysRole(SysRole record);

    List<String> selectByMenuUrl(@Param("menuUrl") String menuUrl);

}