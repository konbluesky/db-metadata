package com.hthjsj.web.component.form;

import com.hthjsj.web.component.ComponentType;
import com.jfinal.kit.Kv;

/**
 * <p> @Date : 2019/10/31 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class TimeBox extends FormField {

    public TimeBox() {
    }

    public TimeBox(String name, String label) {
        super(name, label);
    }

    @Override
    public ComponentType componentType() {
        return ComponentType.TIMEBOX;
    }

    @Override
    public Kv toKv() {
        getViewInject().inject(this, meta, conf, getFieldInject());
        return meta;
    }
}
