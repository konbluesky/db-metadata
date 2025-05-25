package com.github.md.web.user.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.md.web.user.User;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.github.md.web.kit.UtilKit;

import java.util.Collection;
import java.util.List;

/**
 * <p> @Date : 2019/12/16 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class JsonUserPermit implements MRPermit<User, MResource> {

    Multimap<String, String> userPermits = HashMultimap.create();

    /**
     * json数据文件
     *
     * @param filename
     */
    public JsonUserPermit(String filename) {
        String jsonString = UtilKit.loadConfigByFile(filename);
        JSONArray users = JSON.parseObject(jsonString).getJSONArray("rules");
        List<JSONObject> userLists = users.toJavaList(JSONObject.class);
        for (JSONObject u : userLists) {
            Object[] resoutces = u.getJSONArray("resources").toArray(new Object[0]);
            for (Object s : resoutces) {
                userPermits.put(u.getString("userId"), String.valueOf(s));
            }
        }
    }

    /**
     * 对需要放行的资源校验,对于不需要验证的资源,默认放行
     *
     * @param user
     * @param mResource
     *
     * @return
     */
    @Override
    public boolean permit(User user, MResource mResource) {
        if (mResource.needPermit()) {
            Collection<String> rs = userPermits.get(user.userId());
            return rs.contains(mResource.mResourceId());
        }
        return true;
    }
}
