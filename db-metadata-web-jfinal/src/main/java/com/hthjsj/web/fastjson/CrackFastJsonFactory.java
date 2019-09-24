package com.hthjsj.web.fastjson;

import com.jfinal.json.FastJsonFactory;
import com.jfinal.json.Json;

/**
 * <p> Class title: </p>
 * <p> @Describe: </p>
 * <p> @Date : 2019/9/24 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class CrackFastJsonFactory extends FastJsonFactory {
    @Override
    public Json getJson() {
        return new RefSkipFastJson();
    }
}
