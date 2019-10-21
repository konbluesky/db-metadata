package com.hthjsj.web.component.form;

import com.jfinal.kit.Kv;

/**
 * <p> Class title: </p>
 * <p> @Describe: </p>
 * <p> @Date : 2019/10/17 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class Button extends FormField {

    String label;

    public Button(String label) {
        this.label = label;
    }

    @Override
    public Kv toKv() {
        return Kv.create().set("label", label);
    }
}