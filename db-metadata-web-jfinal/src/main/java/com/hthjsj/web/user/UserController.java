package com.hthjsj.web.user;

import com.google.common.base.Preconditions;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

/**
 * <p> @Date : 2019/12/13 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class UserController extends Controller {

    public void login() {
        LoginService loginService = UserManager.me().loginService();
        String uid = getPara(loginService.loginKey());
        String pwd = getPara(loginService.pwdKey());
        Preconditions.checkNotNull(uid, "用户名必须填写");
        Preconditions.checkNotNull(pwd, "密码必须填写");
        User user = loginService.login(uid, pwd);
        renderJson(user != null ? Ret.ok("data", user) : Ret.fail());
    }

    public void logout() {

    }

    public void list() {
        UserService userService = UserManager.me().userService();
        renderJson(Ret.ok("data", userService.findAll()));
    }
}
