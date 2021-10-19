package com.github.md.web.query.dynamic;

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
