package com.github.md.web.query.dynamic;

/**
 * 默认编译使用JFinalEnjoy引擎，该引擎打包在JFinal ActiveRecord Plugin中；
 * <p> @Date : 2021/10/19 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class DefaultCompileRuntimeFactory implements CompileRuntimeFactory {

    @Override
    public CompileRuntime createCompileRuntime() {
        return new JFinalEnjoyCompileRuntime();
    }
}
