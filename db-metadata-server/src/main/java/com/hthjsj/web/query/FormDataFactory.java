package com.hthjsj.web.query;

import com.alibaba.fastjson.JSON;
import com.hthjsj.analysis.db.MetaDataTypeConvert;
import com.hthjsj.analysis.db.SnowFlake;
import com.hthjsj.analysis.meta.IMetaField;
import com.hthjsj.analysis.meta.IMetaObject;
import com.hthjsj.analysis.meta.MetaData;
import com.hthjsj.web.WebException;
import com.hthjsj.web.kit.UtilKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * <p> @Date : 2019/10/31 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class FormDataFactory {

    public static MetaData buildFormData(Map<String, String[]> httpParams, IMetaObject metaObject, boolean isInsert) {
        Kv params = Kv.create().set(UtilKit.toObjectFlat(httpParams));
        MetaData formData = new MetaData();


        for (IMetaField metaField : metaObject.fields()) {

            //转值
            Object castedValue = MetaDataTypeConvert.convert(metaField, params.getStr(metaField.fieldCode()));

            try {
                //主键处理
                if (metaField.isPrimary() && isInsert) {
                    //非联合主键时,根据策略开关(uuid或数值序列)对主键进行赋值
                    if (!metaObject.isMultiplePrimaryKey()) {
                        if (metaObject.configParser().isNumberSequence()) {
                            formData.set(metaField.fieldCode(), SnowFlake.me().nextId());
                        }
                        if (metaObject.configParser().isUUIDPrimary()) {
                            formData.set(metaField.fieldCode(), StrKit.getRandomUUID());
                        }
                    } else {
                        formData.set(metaField.fieldCode(), castedValue);
                    }
                    //自增主键时删除主键字段,交给数据库自增,
                    if (metaObject.configParser().isAutoIncrement()) {
                        formData.remove(metaObject.primaryKey());
                    }
                    continue;
                }
                if (castedValue != null) {
                    formData.set(metaField.fieldCode(), castedValue);
                }
                if (metaField.configParser().isFile()) {

                    List<String> fileConfig = metaField.configParser().fileConfig();
                    List<UploadFile> files = JSON.parseArray(String.valueOf(castedValue), UploadFile.class);

                    for (UploadFile uploadFile : files) {
                        if (fileConfig == null || fileConfig.isEmpty()) {
                            uploadFile.setSeat("default");
                        }
                    }

                    if (files != null && files.size() > 0) {
                        formData.set(metaField.fieldCode(), JSON.toJSONString(files));
                    } else {
                        formData.set(metaField.fieldCode(), "");
                    }
                }
            } catch (MetaDataTypeConvert.MetaDataTypeConvertException e) {
                log.error(e.getMessage(), e);
                throw new WebException("非法转换: 元字段 %s 是%s类型-> %s 失败", metaField.fieldCode(), metaField.dbType().rawData(), (String) castedValue);
            }
        }

        //此处设置updateby和time字段,系统相关的表都有ccuu四个字段
        if (metaObject.isSystem()) {
            if (isInsert) {
                UtilKit.setCreateUser(formData);
            } else {
                UtilKit.setUpdateUser(formData);
            }
        }
        return formData;
    }

    public static void buildUpdateFormData(IMetaObject metaObject, Record record) {
        for (IMetaField metaField : metaObject.fields()) {

            if (metaField.configParser().isFile()) {
                String filepath = record.getStr(metaField.fieldCode());
                if (StrKit.isBlank(filepath)) {
                    filepath = "[]";
                }
                record.set(metaField.fieldCode(), JSON.parseArray(filepath));
            }
        }
    }

    @Data
    public static class UploadFile {

        String name;

        String url;

        String seat;
    }
}
