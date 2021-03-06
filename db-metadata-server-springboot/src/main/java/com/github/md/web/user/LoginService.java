package com.github.md.web.user;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p> @Date : 2019/12/13 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public interface LoginService<U extends User> {

    /**
     * 从Request中获取用户标识时需要用到得tokenKey;
     *
     * @return
     */
    String tokenKey();

    /**
     * 登录时 获取用户名的key
     * 如:username
     *
     * @return
     */
    String loginKey();

    /**
     * 登录时 获取密码的key
     *
     * @return
     */
    String pwdKey();

    String cookieKey();

    U getUser(HttpServletRequest request);

    U login(String username, String password);

    /**
     * 新建用户动作(注册)
     *
     * @param username
     * @param password
     * @param attr
     */
    default boolean register(String username, String password, Map attr) {
        return false;
    }

    /**
     * 如外部已完成用户的login动作,可以将User用户手动登入
     * 方法主要逻辑在于显示得将某个用户注册到验证容器中
     *
     * @param user
     *
     * @return
     */
    default U login(U user) {
        return user;
    }

    default boolean logout(U user) {
        return false;
    }

    default boolean logged(U user) {
        return false;
    }

    default boolean isExpired(U user) {
        return false;
    }
}
