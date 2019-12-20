package com.hthjsj.web;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.hthjsj.AnalysisConfig;
import com.hthjsj.AnalysisManager;
import com.hthjsj.web.auth.JsonUserPermit;
import com.hthjsj.web.auth.MRIntercept;
import com.hthjsj.web.auth.MRManager;
import com.hthjsj.web.auth.jfinal.JFinalResourceLoader;
import com.hthjsj.web.component.Components;
import com.hthjsj.web.controller.CoreRouter;
import com.hthjsj.web.controller.FileViewController;
import com.hthjsj.web.ext.meta.CCUUConfigExtension;
import com.hthjsj.web.ext.meta.InstanceConfigExtension;
import com.hthjsj.web.ext.meta.MetaFieldConfigExtension;
import com.hthjsj.web.feature.FeatureRouter;
import com.hthjsj.web.jfinal.ExceptionIntercept;
import com.hthjsj.web.jfinal.JsonParamIntercept;
import com.hthjsj.web.jfinal.fastjson.CrackFastJsonFactory;
import com.hthjsj.web.jfinal.render.ErrorJsonRenderFactory;
import com.hthjsj.web.kit.Dicts;
import com.hthjsj.web.kit.InitKit;
import com.hthjsj.web.ui.ComputeKit;
import com.hthjsj.web.upload.UploadController;
import com.hthjsj.web.user.UserAuthIntercept;
import com.hthjsj.web.user.UserRouter;
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
        me.add(new CoreRouter());
        me.add(new FeatureRouter());
        if (getPropertyToBoolean(AppConst.NEED_LOGIN)) {
            me.add(new UserRouter());
        }
        me.add("/file", UploadController.class);
        me.add("/fileview", FileViewController.class);
    }

    @Override
    public void configEngine(Engine me) {
    }

    @Override
    public void configPlugin(Plugins me) {
        AnalysisConfig analysisConfig = AnalysisConfig.me();
        analysisConfig.initDefaultDbSource();
        analysisConfig.getPlugins().forEach(p -> me.add(p));
    }

    @Override
    public void onStart() {
        SerializeConfig.getGlobalInstance().put(Record.class, new FastJsonRecordSerializer());

        //component register
        Components.me().init();

        Dicts.me().init();

        //Auto import anyConfig from json file;
        if (getPropertyToBoolean(AppConst.CONFIG_ALLOW_REPLACE)) {
            InitKit.me().importMetaObjectConfig().importInstanceConfig();
        }

        if (getPropertyToBoolean(AppConst.NEED_AUTH)) {
            MRManager mrManager = MRManager.me();
            mrManager.addLoader(new JFinalResourceLoader());
            mrManager.setPermit(new JsonUserPermit("userMRmap.json"));
            mrManager.load();
        }

        AnalysisManager.me().addMetaFieldConfigExtension(new MetaFieldConfigExtension());
        ComputeKit.addInstanceExtension(new InstanceConfigExtension());
        ComputeKit.addInstanceExtension(new CCUUConfigExtension());
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new ExceptionIntercept());
        me.add(new JsonParamIntercept());
        if (getPropertyToBoolean(AppConst.NEED_LOGIN)) {
            me.add(new UserAuthIntercept());
        }
        if (getPropertyToBoolean(AppConst.NEED_AUTH)) {
            me.add(new MRIntercept());
        }
    }

    @Override
    public void configHandler(Handlers me) {
    }
}
