package org.yangyi.project.oauth.entity;

import java.io.Serializable;
import java.util.List;

public class SysUserRole extends SysUser implements Serializable {

    private List<SysRole> sysRoles;

    public List<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

}
