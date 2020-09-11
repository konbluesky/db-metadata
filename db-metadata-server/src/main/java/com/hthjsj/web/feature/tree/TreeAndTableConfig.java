package com.hthjsj.web.feature.tree;

import com.alibaba.fastjson.JSON;
import com.hthjsj.analysis.meta.aop.PointCutFactory;
import com.hthjsj.analysis.meta.aop.QueryPointCut;
import com.hthjsj.web.feature.FeatureConfig;
import com.hthjsj.web.kit.tree.TreeConfig;
import lombok.Data;

/**
 * <p> @Date : 2020/1/22 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class TreeAndTableConfig extends FeatureConfig implements TreeConfigGetter {

    private TreeConfig treeConfig;

    private TableConfig tableConfig;

    private TreeAndTableIntercept intercept;

    @Override
    public TreeConfig getTreeConfig() {
        if (treeConfig == null) {
            treeConfig = JSON.parseObject(getStr("tree"), TreeConfig.class);
        }
        return treeConfig;
    }

    public TableConfig getTableConfig() {
        if (tableConfig == null) {
            tableConfig = JSON.parseObject(getStr("table"), TableConfig.class);
        }
        return tableConfig;
    }

    public TreeAndTableIntercept intercept() {
        if (intercept == null) {
            QueryPointCut queryPointCut = new PointCutFactory(this).queryPointCut();
            intercept = new TreeAndTableIntercept(null, queryPointCut);
        }
        return intercept;
    }

    @Data
    public static class TableConfig {

        private String objectCode;

        private String primaryKey;

        private String foreignFieldCode;
    }
}
