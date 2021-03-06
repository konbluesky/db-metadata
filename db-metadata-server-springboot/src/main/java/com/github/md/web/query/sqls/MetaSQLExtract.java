package com.github.md.web.query.sqls;

import com.github.md.analysis.meta.IMetaField;

import java.util.Map;

/**
 * <p> Class title: </p>
 * <p> @Describe: </p>
 * <p> @Date : 2019/10/16 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public abstract class MetaSQLExtract implements SQLExtract {

    public static final String SQL_PREFIX = "sql_";

    private boolean mutiple = false;

    public abstract void init(IMetaField metaField, Map<String, Object> httpParams);

    @Override
    public void loadOfHttpParams(Map<String, Object> httpParams) {

    }

    public boolean isMutiple() {
        return mutiple;
    }

    protected void setMutiple(boolean m) {
        mutiple = m;
    }
}
