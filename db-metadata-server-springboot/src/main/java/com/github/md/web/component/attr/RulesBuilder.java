package com.github.md.web.component.attr;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ObjectArrays;
import com.github.md.analysis.meta.IMetaField;
import com.github.md.analysis.kit.Kv;

import java.util.Collection;

/**
 * 表单规则构建工具
 * <p> @Date : 2019/11/15 </p>
 * <p> @Project : db-meta-serve</p>
 * <pre>
 *     https://github.com/yiminghe/async-validator
 * </pre>
 * <p> @author konbluesky </p>
 */
public class RulesBuilder {

    private final static String[] BLUR = new String[] { "blur" };

    private final static String[] CHANGE = new String[] { "change" };

    Multimap<String, Kv> rules = HashMultimap.create();

    public RulesBuilder required(IMetaField metaField) {
        if (metaField.dbType().isNumber()) {
            return required("number", metaField.fieldCode(), metaField.cn());
        } else if (metaField.dbType().isText()) {
            return required("string", metaField.fieldCode(), metaField.cn());
        }
        return required(null, metaField.fieldCode(), metaField.cn());
    }

    public RulesBuilder required(String type, String fieldCode, String cn) {
        rules.put(fieldCode, makeRule(type, true, String.format("[%s]是必须填写的", cn), BLUR));
        return this;
    }

    public RulesBuilder email(IMetaField metaField) {
        return email(metaField.fieldCode(), metaField.cn());
    }

    public RulesBuilder email(String fieldCode, String cn) {
        rules.put(fieldCode, makeRule("email", true, String.format("[%s]是必须填写的", cn), ObjectArrays.concat(BLUR, CHANGE, String.class)));
        return this;
    }

    public Kv buildObj() {
        Kv kv = Kv.create();
        rules.asMap().forEach((key, value) -> {
            kv.set(key, value);
        });
        return kv;
    }

    public Collection<Kv> buildRules(String key) {
        return rules.get(key);
    }

    private Kv makeRule(String type, Boolean required, String message, String[] trigger) {
        return Kv.create().setIfNotBlank("type", type).setIfNotNull("required", required).setIfNotBlank("message", message).setIfNotNull("trigger", trigger);
    }
}
