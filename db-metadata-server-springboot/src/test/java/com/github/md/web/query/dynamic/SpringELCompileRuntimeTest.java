package com.github.md.web.query.dynamic;

import com.github.md.web.query.dynamic.support.DefaultCompileRuntimeFactory;
import com.github.md.web.query.dynamic.support.SpringELCompileRuntimeFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

/**
 * <p> @Date : 2021/10/19 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
class SpringELCompileRuntimeTest {

    @Test
    void compile1() {
        /* JFinal Enjoy */
        CompileRuntimeFactory factory = new DefaultCompileRuntimeFactory();
        CompileRuntime compileRuntime = factory.createCompileRuntime();
        String result = compileRuntime.compile("hello here is#(1+1)");
        log.info(result);
        Assert.isTrue("hello here is2".equals(result));
    }

    @Test
    void compile2() {
        /* Spring EL */
        CompileRuntimeFactory factory = new SpringELCompileRuntimeFactory();
        CompileRuntime compileRuntime = factory.createCompileRuntime();
        String result = compileRuntime.compile("'hello here is'+(1+1)");
        log.info(result);
        Assert.isTrue("hello here is2".equals(result));
    }
}