package com.hthjsj.web;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.hthjsj.AnalysisConfig;
import com.hthjsj.web.component.Components;
import com.hthjsj.web.controller.*;
import com.hthjsj.web.jfinal.ExceptionIntercept;
import com.hthjsj.web.jfinal.JsonParamIntercept;
import com.hthjsj.web.jfinal.UserSettingIntercept;
import com.hthjsj.web.jfinal.fastjson.CrackFastJsonFactory;
import com.hthjsj.web.jfinal.render.ErrorJsonRenderFactory;
import com.hthjsj.web.module.ModuleRouter;
import com.hthjsj.web.upload.UploadController;
import com.jfinal.config.*;
import com.jfinal.json.FastJsonRecordSerializer;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

/**
 * <p> @Date : 2019-08-22 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class AppWebConfig extends JFinalConfig {

    public static void main(String[] args) {
        UndertowServer.start(AppWebConfig.class, 8888, true);
    }

    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("config.properties");
        me.setDevMode(getPropertyToBoolean("devMode", false));
        me.setJsonFactory(new CrackFastJsonFactory());
        me.setRenderFactory(new ErrorJsonRenderFactory());
    }

    @Override
    public void configRoute(Routes me) {
        me.setMappingSuperClass(true);
        me.addInterceptor(new JsonParamIntercept());
        me.add("/db", DBController.class);
        me.add("/meta", MetaController.class);
        me.add("/component", ComponentController.class);
        me.add("/component/options", OptionsController.class);
        me.add("/table", TableController.class);
        me.add("/form", FormController.class);
        me.add("/dict", DictController.class);
        me.add("/check", ValidatorController.class);
        me.add("/file", UploadController.class);
        me.add(new ModuleRouter());
    }

    @Override
    public void configEngine(Engine me) {
    }

    @Override
    public void configPlugin(Plugins me) {
        AnalysisConfig analysisConfig = AnalysisConfig.me();
        analysisConfig.getPlugins().forEach(p -> me.add(p));
    }

    @Override
    public void onStart() {
        SerializeConfig.getGlobalInstance().put(Record.class, new FastJsonRecordSerializer());

        //component register
        Components.me().init();
        Dicts.me().init();
        if (getPropertyToBoolean(AppConst.CONFIG_ALLOW_REPLACE)) {
            InitKit.me().importMetaObjectConfig().importInstanceConfig();
        }
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new ExceptionIntercept());
        me.add(new UserSettingIntercept());
    }

    @Override
    public void configHandler(Handlers me) {
    }
}
