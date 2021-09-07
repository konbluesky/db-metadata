package com.hthjsj;

import com.hthjsj.analysis.db.registry.DataSourceManager;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbPro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p> Class title: </p>
 * <p> @Describe: </p>
 * <p> @Date : 2019-08-20 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
@Configuration
//@ConditionalOnBean(DataSourceManager.class)
public class SpringAnalysisManager {

    private static SpringAnalysisManager me;

    private DataSourceManager dataSourceManager;

    public SpringAnalysisManager(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
    }

    public static SpringAnalysisManager me() {
        return me;
    }

    public DbPro dbMain() {
        return Db.use(dataSourceManager.mainSource().schemaName());
    }

    @PostConstruct
    private void init() {
        me = this;
    }
}
