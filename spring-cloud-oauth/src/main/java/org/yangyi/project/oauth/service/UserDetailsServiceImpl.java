package org.yangyi.project.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yangyi.project.oauth.dao.SysUserDao;
import org.yangyi.project.oauth.entity.SysUserRole;

import java.util.Objects;

/**
 * 自定义用户验证数据
 * 从数据库中查询
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private SysUserDao sysUserDao;

    @Autowired
    public void setSysUserDao(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserRole sysUserRole = sysUserDao.selectUserAndRoleByUsername(username);
        if (Objects.isNull(sysUserRole))
            throw new UsernameNotFoundException("用户不存在!");

        return new User(
                sysUserRole.getUsername(),
                sysUserRole.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")
        );
    }
}
