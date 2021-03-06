package com.github.md.web.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.github.md.analysis.kit.Kv;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * <p> @Date : 2019/10/18 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class UserIntercept /*implements Interceptor */{

    private final List<String> skipPathPatterns = Lists.newArrayList(UserRouter.URL_LOGIN);

    public static User staticUser = new User() {

        @Override
        public String userId() {
            return "db-meta-web-devUser";
        }

        @Override
        public String userName() {
            return null;
        }

        @Override
        public Kv attrs() {
            return null;
        }

        @Override
        public Kv attrs(Map attrs) {
            return null;
        }
    };

    public UserIntercept() {
    }

//    @Override
//    public void intercept(Invocation inv) {
        /**
         * 获取 LoginService ,进行登录校验
         * 1. 获取LoginService实例
         * 2. 从Request中取得用户;
         *  2.1 取id|token|或其他
         *  2.2 从某个容器中获取用户信息(redis|db|缓存|其他)
         * 3. 判断用户是否已登录,是否过期
         *  3.1 已登录,未过期 -> 放行
         *  3.2 未登录->拒绝
         *  3.3 已登录,已过期 -> 拒绝
         *  > 已登录和过期 ,用两种状态表示, 用是否过期来表示用户是否登录虽然也可以,但是语义不明确
         * 4. 放行后更新用户过期时间.
         */
//        User user = null;
//        try {
//            // 放行
//            if (PatternPathMatcher.matchAny(inv.getActionKey(),
//                    skipPathPatterns.toArray(new String[skipPathPatterns.size()]))) {
//                inv.invoke();
//                return;
//            }
//
//            LoginService<User> loginService = UserManager.me().loginService();
//            user = loginService.getUser(inv.getController().getRequest());
//            //开发模式时 指定开发用户
//            if (JFinal.me().getConstants().getDevMode()) {
//                if (user == null) {
//                    user = staticUser;
//                }
//            }
//            if (user != null) {
//                UserThreadLocal.setUser(user);
//                inv.invoke();
//                inv.getController().setCookie(loginService.cookieKey(), user.userId(), (int) TimeUnit.HOURS.toSeconds(6));
//            } else {
//                throw new UserException("未从请求内发现有效用户标志,请检查参数:%s", loginService.tokenKey()).loginError();
//            }
//        } finally {
//            UserThreadLocal.removeUser(user);
//        }
//    }

    public void addSkipPathPatterns(String... skipPathPatterns) {
        Preconditions.checkNotNull(skipPathPatterns);
        this.skipPathPatterns.addAll(Lists.newArrayList(skipPathPatterns));
    }
}
