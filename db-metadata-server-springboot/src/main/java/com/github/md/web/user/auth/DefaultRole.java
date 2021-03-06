package com.github.md.web.user.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> @Date : 2020/8/12 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
class DefaultRole implements MRRole {

    private final List<Permission> innerPermissionList;

    private final String code;

    private final String name;

    private Permission[] permissions;

    public DefaultRole(String code, String name) {
        this.code = code;
        this.name = name;
        this.innerPermissionList = new ArrayList<>();
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Permission[] permissions() {
        if (!innerPermissionList.isEmpty()) {
            return innerPermissionList.toArray(permissions);
        }
        return permissions;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        for (Permission p : permissions) {
            if (p.code().equalsIgnoreCase(permission.code().toLowerCase()) || p.name().equalsIgnoreCase(permission.name().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void addPermission(Permission permission) {
        innerPermissionList.add(permission);
    }
}
