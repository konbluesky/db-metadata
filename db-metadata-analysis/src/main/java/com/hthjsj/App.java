package com.hthjsj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hthjsj.analysis.meta.DbMetaService;
import com.hthjsj.analysis.meta.MetaObject;
import com.hthjsj.analysis.meta.MetaObjectDBAdapter;
import com.jfinal.aop.Aop;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {

        AppConfig appConfig = new AppConfig();
        appConfig.start();

//        Table table = new MysqlService().getTable("metadata", "meta_field");
//        System.out.println(JSON.toJSON(table));
//
//        IMetaObject IMetaObject = new DbMetaService().findByCode("sys_meta_object");
//        System.out.println(JSON.toJSONString(IMetaObject));
        DbMetaService dbMetaService = Aop.get(DbMetaService.class);
        MetaObject metaObject = (MetaObject) dbMetaService.importFromTable("metadata", "meta_object");
        System.out.println(JSON.toJSONString(metaObject, SerializerFeature.DisableCircularReferenceDetect));

        MetaObjectDBAdapter adapter = new MetaObjectDBAdapter(metaObject);


        dbMetaService.deleteMetaObject(metaObject);
        dbMetaService.saveMetaObject(adapter, true);
    }


    public void testSaveMetaObject() {


    }


}
