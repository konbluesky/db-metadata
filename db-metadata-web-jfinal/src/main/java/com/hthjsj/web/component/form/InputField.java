package com.hthjsj.web.component.form;

import com.hthjsj.analysis.meta.IMetaField;
import com.jfinal.kit.Kv;

/**
 * <p> Class title: </p>
 * <p> @Describe: </p>
 * <p> @Date : 2019/10/16 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class InputField extends FormField {

    String name;

    String label;

    IMetaField metaField;

    Kv metaFieldConfig = Kv.create();

    public InputField(IMetaField metaField) {
        this.metaField = metaField;
        name = metaField.cn();
        label = metaField.en();
        metaFieldConfig = (Kv) metaField.config();
    }

    public InputField(String name, String label) {
        this.name = name;
        this.label = label;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String code() {
        return name;
    }

    @Override
    public String type() {
        return "TextBox";
    }

    @Override
    public Kv toKv() {
        metaFieldConfig.set("component_name", type());
        metaFieldConfig.set("name", name);
        metaFieldConfig.set("label", label);
        return metaFieldConfig;
    }
}
