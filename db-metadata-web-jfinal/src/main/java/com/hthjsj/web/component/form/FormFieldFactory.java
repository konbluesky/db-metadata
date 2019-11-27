package com.hthjsj.web.component.form;

import com.hthjsj.analysis.component.ComponentType;
import com.hthjsj.analysis.component.ViewContainer;
import com.hthjsj.analysis.meta.IMetaField;
import com.hthjsj.web.component.render.MetaFormFieldRender;
import com.jfinal.kit.Kv;

/**
 * <p> @Date : 2019/10/30 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class FormFieldFactory {

    static <T extends FormField> T create(T formField, IMetaField metaField, Kv instanceFieldConfig) {
        formField.setRender(new MetaFormFieldRender<T>(metaField, formField, instanceFieldConfig));
        return formField;
    }

    /**
     * <pre>
     * 说明: 区别于createFormField 传入的配置 为全局
     *
     * instanceFieldConfig 数据格式
     *         [key]        [value]
     *      fieldCode -> meta_component.config
     * </pre>
     *
     * @param metaField
     * @param globalConfig
     *
     * @return
     */
    public static FormField createFormFieldDefault(IMetaField metaField, Kv globalConfig) {
        globalConfig.set("name", metaField.fieldCode()).set("label", metaField.cn());
        return createFormField(metaField, globalConfig);
    }

    /**
     * 创建带容器的FormField
     *
     * @param metaField
     * @param globalConfig
     * @param viewContainer
     *
     * @return
     */
    public static FormField createFormFieldInContainer(IMetaField metaField, Kv globalConfig, ViewContainer viewContainer) {
        FormField formField = createFormField(metaField, globalConfig);
        formField.setViewContainer(viewContainer);
        return formField;
    }

    /**
     * <pre>
     * 说明:
     * instanceFieldConfig 数据格式
     *         [key]        [value]
     *      fieldCode -> meta_component_instance.config
     * </pre>
     *
     * @param metaField
     * @param instanceFieldConfig 假定所有列都有对应存在的配置
     *
     * @return
     */
    public static FormField createFormField(IMetaField metaField, Kv instanceFieldConfig) {
        ComponentType type = ComponentType.V(instanceFieldConfig.getStr("component_name"));
        TextBox textBox = new TextBox(metaField.fieldCode(), metaField.cn());
        switch (type) {
            case TEXTBOX:
                return create(textBox, metaField, instanceFieldConfig);
            case CHECKBOX:
                CheckBox checkBox = new CheckBox(metaField.fieldCode(), metaField.cn());
                return create(checkBox, metaField, instanceFieldConfig);
            case DROPDOWN:
                DropDownBox dropDownBox = new DropDownBox(metaField.fieldCode(), metaField.cn());
                return create(dropDownBox, metaField, instanceFieldConfig);
            case RADIOBOX:
                RadioBox radioBox = new RadioBox(metaField.fieldCode(), metaField.cn());
                return create(radioBox, metaField, instanceFieldConfig);
            case NUMBERBOX:
                NumberBox numberBox = new NumberBox(metaField.fieldCode(), metaField.cn());
                return create(numberBox, metaField, instanceFieldConfig);
            case BOOLBOX:
                BoolBox boolBox = new BoolBox(metaField.fieldCode(), metaField.cn());
                return create(boolBox, metaField, instanceFieldConfig);
            case TEXTAREABOX:
                TextAreaBox textAreaBox = new TextAreaBox(metaField.fieldCode(), metaField.cn());
                return create(textAreaBox, metaField, instanceFieldConfig);
            case DATEBOX:
                DateBox dateBox = new DateBox(metaField.fieldCode(), metaField.cn());
                return create(dateBox, metaField, instanceFieldConfig);
            case TIMEBOX:
                TimeBox timeBox = new TimeBox(metaField.fieldCode(), metaField.cn());
                return create(timeBox, metaField, instanceFieldConfig);
            case DATETIMEBOX:
                DateTimeBox dateTimeBox = new DateTimeBox(metaField.fieldCode(), metaField.cn());
                return create(dateTimeBox, metaField, instanceFieldConfig);
            case JSONBOX:
                JsonBox jsonBox = new JsonBox(metaField.fieldCode(), metaField.cn());
                return create(jsonBox, metaField, instanceFieldConfig);
            default:
                break;
        }
        //if type == unknow  use TextBox
        return create(textBox, metaField, instanceFieldConfig);
    }
}
