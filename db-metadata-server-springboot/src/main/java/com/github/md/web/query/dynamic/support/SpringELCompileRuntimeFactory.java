package com.github.md.web.query.dynamic.support;

import com.github.md.web.query.dynamic.CompileRuntime;
import com.github.md.web.query.dynamic.CompileRuntimeFactory;

/**
 * <p> @Date : 2021/10/19 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class SpringELCompileRuntimeFactory implements CompileRuntimeFactory {

    @Override
    public CompileRuntime createCompileRuntime() {
        return new SpringELCompileRuntime();
    }
}
