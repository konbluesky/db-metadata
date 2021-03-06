package com.github.md.web.user.support.local;

import com.github.md.web.user.User;
import com.github.md.web.kit.UtilKit;
import com.github.md.web.user.auth.MRRole;
import com.github.md.web.user.role.UserWithRolesWrapper;
import com.github.md.analysis.kit.Kv;

import java.util.Map;

/**
 * <p> @Date : 2019/12/13 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class LocalUser implements User, UserWithRolesWrapper {

    Kv attrs;

    MRRole[] roles;

    public LocalUser(Map attr) {
        this.attrs = Kv.create().set(attr);
    }

    public LocalUser(Map attr, MRRole role) {
        this.attrs = Kv.create().set(attr);
        this.roles = new MRRole[]{role};
    }

    public LocalUser(Map attr, MRRole... roles) {
        this.attrs = Kv.create().set(attr);
        this.roles = roles;
    }

    @Override
    public String userId() {
        return attrs.getStr("userId");
    }

    @Override
    public String userName() {
        return attrs.getStr("userName");
    }

    public String password() {
        return attrs.getStr("password");
    }

    @Override
    public Kv attrs() {
        return attrs;
    }

    @Override
    public Kv attrs(Map attrs) {
        UtilKit.deepMerge(this.attrs, attrs, true);
        return this.attrs;
    }

    @Override
    public MRRole[] roles() {
        return roles;
    }

    @Override
    public boolean hasRole(String nameOrCode) {
        for (MRRole role : roles) {
            if (role.name().equalsIgnoreCase(nameOrCode.toLowerCase()) || role.code().equalsIgnoreCase(nameOrCode.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
