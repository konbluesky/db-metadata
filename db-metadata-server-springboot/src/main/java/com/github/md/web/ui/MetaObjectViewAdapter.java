package com.github.md.web.ui;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Maps;
import com.github.md.analysis.component.Component;
import com.github.md.analysis.meta.IMetaObject;
import com.github.md.web.kit.UtilKit;
import com.github.md.analysis.kit.Kv;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * MetaObjectViewAdapter 同时持有metaObject,component,各层config 实例
 * 方便逻辑流转运算;
 * <p> @Date : 2019/11/13 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MetaObjectViewAdapter {

    @Getter
    private final IMetaObject metaObject;

    @Getter
    @JSONField(serialize = false)
    private final Component component;

    @Getter
    private final List<MetaFieldViewAdapter> fields;

    @Getter
    private final Kv objectConfig;

    @Getter
    private final Kv globalComponentConfig;

    @Getter
    private final ComponentInstanceConfig instanceConfig;

    @Getter
    private final Map<String, MetaFieldViewAdapter> fieldsMap;

    MetaObjectViewAdapter(IMetaObject metaObject, Component component, Kv globalComponentConfig, ComponentInstanceConfig componentInstanceConfig,
                          List<MetaFieldViewAdapter> fields) {
        this.metaObject = metaObject;
        this.component = component;
        this.fields = fields;

        //init config
        this.objectConfig = UtilKit.getKv(metaObject.config());
        this.globalComponentConfig = globalComponentConfig;
        this.instanceConfig = componentInstanceConfig;
        this.fieldsMap = Maps.newHashMap();
        for (MetaFieldViewAdapter metaFieldViewAdapter : fields) {
            metaFieldViewAdapter.setContainer(this);
            fieldsMap.put(metaFieldViewAdapter.getMetaField().fieldCode(), metaFieldViewAdapter);
        }

        if (metaObject.fields().size() != fields.size()) {
            log.error("元对象 {} 的元字段和元字段实例配置({})数量不匹配! 请检查！", metaObject.code(), component.code());
        }
    }

    /**
     * 获取元子段
     *
     * @param fieldCode
     *
     * @return
     */
    public MetaFieldViewAdapter getFieldAdapter(String fieldCode) {
        return fieldsMap.get(fieldCode);
    }
}
