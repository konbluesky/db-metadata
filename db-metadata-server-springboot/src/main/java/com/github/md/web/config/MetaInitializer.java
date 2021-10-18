package com.github.md.web.config;

import com.github.md.analysis.AnalysisProperties;
import com.github.md.analysis.SpringAnalysisManager;
import com.github.md.web.AppConst;
import com.github.md.web.config.register.PathCustomizer;
import com.github.md.web.config.register.PrefixPathCustomizer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Specially initialize some classes required by MetaServer
 * <p> @Date : 2021/9/8 </p>
 * <p> @Project : db-metadata-server-springboot</p>
 *
 * <p> @author konbluesky </p>
 */
@Configuration
@Slf4j
public class MetaInitializer {

    @Bean
    @ConfigurationProperties("md")
    public MetaProperties metaProperties(AnalysisProperties analysisProperties) {
        return new MetaProperties(analysisProperties);
    }

    /** Some logic that needs to be triggered automatically after the program is started */
    @Bean
    public MetaBootstrap metaBootstrap(MetaProperties metaProperties) {
        return new MetaBootstrap(metaProperties);
    }

    @Bean
    public MetaServerManager metaServerManager(SpringAnalysisManager analysisManager, MetaProperties metaProperties) {
        return new MetaServerManager(analysisManager, metaProperties);
    }

    @Bean
    public QuickJudge quickJudge(MetaServerManager metaServerManager) {
        return new QuickJudgeImpl(metaServerManager);
    }

    @Bean
    @ConditionalOnProperty(name = { "md.server.url-prefix" })
    public PathCustomizer pathCustomizer(MetaProperties metaProperties) {
        String contextUrlPrefix = metaProperties.getServer().getUrlPrefix();
        Preconditions.checkArgument(StrKit.notBlank(contextUrlPrefix), "[md.server.context-url-prefix] must be set; \nexample: 'md.server.url-prefix = /v1'");
        return new PrefixPathCustomizer(contextUrlPrefix,
                                        Lists.asList(AppConst.DEFAULT_URL_EFFECT_JAVA_PACKAGE, metaProperties.getServer().getUrlEffectJavaPackage())
                                             .toArray(new String[0]));
    }
}
