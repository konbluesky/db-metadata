package com.hthjsj.analysis.meta;

import com.jfinal.kit.Kv;

/**
 * <p> @Date : 2019-08-23 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class MetaConfigFactory {

    public static MetaObjectConfigParse createV1ObjectConfig(String objectCode, String isUUIDPri) {
        Kv config = Kv.create();
        config.set("isUUIDPrimary", Boolean.parseBoolean(isUUIDPri));
        config.set("objectCode", objectCode);
        return new MetaObjectConfigParse(config);
    }

    public static MetaFieldConfigParse createV1FieldConfig(MetaField metaField, String defaultValue, String isNUll) {
        Kv config = Kv.create();
        config.set("isNullable", "yes".equalsIgnoreCase(isNUll));
        config.set("defaultValue", defaultValue == null ? "" : defaultValue);
        config.set("objectCode", metaField.objectCode());
        config.set("fieldCode", metaField.fieldCode());
        config.set("isMultiple", false);

        //主键:新增不可见,更新只读
        if (metaField.isPrimary()) {
            config.set("addStatus", MetaFieldConfigParse.DISABLE);
            config.set("updateStatus", MetaFieldConfigParse.READONLY);
        } else {
            config.set("addStatus", MetaFieldConfigParse.NORMAL);
            config.set("updateStatus", MetaFieldConfigParse.NORMAL);
        }

        //TODO 初始化field配置时 , 名称包含file的 默认设定isFile = true
        if (metaField.fieldCode().contains("file")) {
            config.set("isFile", true);
        }

        config.set("isListShow", true);
        config.set("isSearch", true);
        return new MetaFieldConfigParse(config);
    }
}
