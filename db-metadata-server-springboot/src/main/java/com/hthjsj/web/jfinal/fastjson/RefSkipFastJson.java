package com.hthjsj.web.jfinal.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jfinal.json.FastJson;

/**
 * <p> @Date : 2019/9/24 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class RefSkipFastJson extends FastJson {

    @Override
    public String toJson(Object object) {
        // 优先使用对象级的属性 datePattern, 然后才是全局性的 defaultDatePattern
        String dp = datePattern != null ? datePattern : getDefaultDatePattern();
        if (dp == null) {
            return JSON.toJSONString(object,
                                     SerializerFeature.DisableCircularReferenceDetect,
                                     SerializerFeature.WriteNullListAsEmpty,
                                     SerializerFeature.WriteNullStringAsEmpty,
                                     SerializerFeature.WriteMapNullValue);
        } else {
            // return JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat);
            return JSON.toJSONStringWithDateFormat(object,
                                                   dp,
                                                   SerializerFeature.DisableCircularReferenceDetect,
                                                   SerializerFeature.WriteDateUseDateFormat,
                                                   SerializerFeature.WriteNullListAsEmpty,
                                                   SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
            //            SerializerFeature.WriteNonStringValueAsString
            //            按照toString方式获取对象字面值
            //            会导致 true -> "true"  ,1->"1"
        }
    }
}