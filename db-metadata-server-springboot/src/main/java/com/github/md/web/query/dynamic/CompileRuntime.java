package com.github.md.web.query.dynamic;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> @Date : 2021/10/19 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public interface CompileRuntime {

    /**
     * 根据CompileManager.getCompileDataStructure中注册的内容，编译sql模板
     *
     * @param sqlTemplate
     *
     * @return
     */
    String compile(String sqlTemplate);

    /**
     * 传入指定Context上下文，编译sql模板
     *
     * @param sqlTemplate
     * @param context
     *
     * @return
     */
    String compile(String sqlTemplate, Context context);

    String compile(String sqlTemplate, HttpServletRequest request);

    String compile(String sqlTemplate, HttpServletRequest request, Context context);
}
