package com.hthjsj.web.controller;

import com.google.common.collect.Lists;
import com.hthjsj.web.ServiceManager;
import com.hthjsj.web.component.Components;
import com.hthjsj.web.query.QueryHelper;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

public class ComponentController extends FrontRestController {

    @Override
    public void api() {
        renderJson(Components.me().getRegistry());
    }

    public void init() {
        Components.me().init();
        renderJson(Ret.ok());
    }

    @Override
    public void list() {
        List<Record> components = ServiceManager.componentService().listComponents();
        List<Kv> results = Lists.newArrayList();
        components.forEach(r -> {
            results.add(Kv.create().set("key", r.getStr("cn")).set("value", r.getStr("en")));
            //            results.add(r.getStr("code"));
        });
        renderJson(Ret.ok("data", results));
    }

    public void load() {
        /**
         * object Code
         * component Type
         *
         */
        QueryHelper queryHelper = new QueryHelper(this);
        String objectCode = queryHelper.getObjectCode();
        String compCode = queryHelper.getComponentCode();
        /**
         * return
         * {
         *  config:{
         *
         *  }
         * }
         */
        renderJson(Ret.ok("data", ServiceManager.componentService().loadObjectConfig(compCode, objectCode)));
    }

    @Override
    public void toAdd() {

    }

    @Override
    public void doAdd() {
        /**
         *
         */
    }

    /**
     * set Component config
     */
    public void config() {
        /**
         * object code
         * component Type config string;
         */
    }

    /**
     * The component config default value;
     */
    public void tmpl() {
        /**
         * component Type
         */
        String component = new QueryHelper(this).getComponentType();
        renderJson(Ret.ok("data", ServiceManager.componentService().loadDefault(component)));
        /**
         * return
         * {
         *    field 1:"",
         *    field 2:"",
         *    field 3:"",
         *    field 4:"",
         *    field 5:"",
         *    base :""
         * }
         */
    }
}
