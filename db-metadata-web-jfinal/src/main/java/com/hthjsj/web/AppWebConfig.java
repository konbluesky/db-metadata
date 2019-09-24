package com.hthjsj.web;

import com.hthjsj.AnalysisConfig;
import com.hthjsj.web.fastjson.CrackFastJsonFactory;
import com.jfinal.config.*;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

/**
 * <p> Class title: </p>
 * <p> @Describe: </p>
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
        me.setJsonFactory(new CrackFastJsonFactory());


    }
    
    @Override
    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
        me.add("/meta", MetaController.class);
    }
    
    @Override
    public void configEngine(Engine me) {
    
    }
    
    @Override
    public void configPlugin(Plugins me) {
        AnalysisConfig analysisConfig = new AnalysisConfig();
        analysisConfig.getPlugins().forEach(p -> me.add(p));

    }
    
    @Override
    public void configInterceptor(Interceptors me) {

    }


    @Override
    public void configHandler(Handlers me) {
    
    }
}
