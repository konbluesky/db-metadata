package com.github.md.web.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.md.web.config.json.FastJsonRecordSerializer;
import com.github.md.web.config.json.JsonParameterToMapHandler;
import com.github.md.web.config.register.DynamicRegisterControllerHandlerMapping;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p> @Date : 2021/9/10 </p>
 * <p> @Project : db-metadata-server-springboot</p>
 *
 * <p> @author konbluesky </p>
 */
@Configuration
@Slf4j
public class MetaServerWebMvcConfigurer implements WebMvcConfigurer, WebMvcRegistrations {

    @Autowired
    MetaProperties metaProperties;

    /**
     * 可定制MetaServer系统URl的前缀
     *
     * @return
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        if (StrKit.notBlank(metaProperties.getServer().getUrlPrefix())) {
            return new DynamicRegisterControllerHandlerMapping();
        } else {
            return new RequestMappingHandlerMapping();
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String defaultPatterns = "/**";
        if (StrKit.notBlank(metaProperties.getServer().getUrlPrefix())) {
            defaultPatterns = metaProperties.getServer().getUrlPrefix();
            log.info("Bind [JsonParameterToMapHandler] to [{}] series url", defaultPatterns);
        }
        InterceptorRegistration jsonParameterToMap = registry.addInterceptor(new JsonParameterToMapHandler());
        jsonParameterToMap.addPathPatterns(defaultPatterns);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        /**
         * SpringBoot 默认使用Jackson作为json序列化工具,为了避免干扰需要将Jackson关掉
         * 关掉有2种方式:
         * 1. 从pom.xml中排除jackson引用
         * 2. 代码中动态remove
         * 这里使用第二种
         */
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                iterator.remove();
            }
        }

        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 保留map空的字段
                SerializerFeature.WriteMapNullValue,
                // 将String类型的null转成""
                SerializerFeature.WriteNullStringAsEmpty,
                // 将Number类型的null转成0
                SerializerFeature.WriteNullNumberAsZero,
                // 将List类型的null转成[]
                SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
                SerializerFeature.WriteNullBooleanAsFalse,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect);

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        // 支持序列化 ActiveRecord 的 Record 类型
        SerializeConfig.getGlobalInstance().put(Record.class, new FastJsonRecordSerializer());
        converters.add(converter);
    }
}
