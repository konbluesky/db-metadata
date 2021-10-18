package com.github.md.web;

import com.google.common.collect.HashBasedTable;

/**
 * <p> @Date : 2019/11/20 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class AppConst {

    public static final String VERSION = "1.0";

    public static final HashBasedTable<String, String, Boolean> SYS_TABLE = HashBasedTable.create();

    public static final String INITABLE = "initable";

    /* Packages used by the default Controller */
    public static final String DEFAULT_URL_EFFECT_JAVA_PACKAGE = "com.github.md.web";

    static {
        SYS_TABLE.put("meta_object", INITABLE, true);
        SYS_TABLE.put("meta_field", INITABLE, true);
        SYS_TABLE.put("meta_component", INITABLE, true);
        SYS_TABLE.put("meta_component_instance", INITABLE, true);
        SYS_TABLE.put("meta_router", INITABLE, true);
        SYS_TABLE.put("meta_menu", INITABLE, true);
        SYS_TABLE.put("meta_feature", INITABLE, true);
        SYS_TABLE.put("meta_config", INITABLE, true);
        SYS_TABLE.put("change_log", INITABLE, false);
        SYS_TABLE.put("meta_exception", INITABLE, false);
        SYS_TABLE.put("meta_dict", INITABLE, false);
    }
}
