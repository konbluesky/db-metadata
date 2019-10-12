package com.hthjsj.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Ret;

import java.util.Arrays;

/**
 * <p> @Date : 2019/10/9 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public abstract class FrontRestController extends Controller implements FrontRest {

    @Override
    public Ret index() {
        list();
        return null;
    }

    @Override
    public Ret toAdd() {
        renderJson(faildMsgInfo());
        return null;
    }

    @Override
    public Ret doAdd() {
        renderJson(faildMsgInfo());
        return null;
    }

    @Override
    public Ret toUpdate() {
        renderJson(faildMsgInfo());
        return null;
    }

    @Override
    public Ret doUpdate() {
        renderJson(faildMsgInfo());
        return null;
    }

    @Override
    public Ret detail() {
        renderJson(faildMsgInfo());
        return null;
    }

    @Override
    public Ret delete() {
        renderJson(faildMsgInfo());
        return null;
    }

    @Override
    public Ret list() {
        renderJson(faildMsgInfo());
        return null;
    }

    Ret faildMsgInfo() {
        Ret ret = Ret.fail();
        if (JFinal.me().getConstants().getDevMode()) {
            ret.set("request_uri", getRequest().getRequestURI());
        }
        ret.set("msg", "not implementation!");
        return ret;
    }

    void renderJson(Object data, String... excludes) {
        SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter();
        simplePropertyPreFilter.getExcludes().addAll(Arrays.asList(excludes));
        renderJson(JSON.toJSONString(data, simplePropertyPreFilter));
    }
}
