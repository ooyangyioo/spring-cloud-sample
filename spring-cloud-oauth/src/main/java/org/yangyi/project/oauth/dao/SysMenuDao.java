package org.yangyi.project.oauth.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.oauth.entity.SysMenu;

import java.util.List;

@Mapper
public interface SysMenuDao {

    @Delete({"delete from sys_menu where menu_id = #{menuId}"})
    int deleteById(Long menuId);

    int insert(SysMenu row);

    @Select({"select * from sys_menu where menu_id = #{menuId}"})
    SysMenu selectById(Long menuId);

    @Select({"select * from sys_menu"})
    List<SysMenu> all();

    int update(SysMenu row);

    List<SysMenu> selectByRoleCode(@Param("roleCode") String roleCode);

}