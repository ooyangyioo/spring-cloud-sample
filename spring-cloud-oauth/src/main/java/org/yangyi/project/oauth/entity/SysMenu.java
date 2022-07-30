package org.yangyi.project.oauth.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.yangyi.project.oauth.mybatis.AutoId;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysMenu implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @AutoId
    private Long menuId;

    private String menuName;

    private String menuUrl;

    private String menuTag;

    private static final long serialVersionUID = 1L;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    public String getMenuTag() {
        return menuTag;
    }

    public void setMenuTag(String menuTag) {
        this.menuTag = menuTag == null ? null : menuTag.trim();
    }

}