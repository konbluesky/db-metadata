package com.github.md.web.query.dynamic;

import com.github.md.web.query.dynamic.support.DefaultCompileRuntimeFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p> @Date : 2020/8/12 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class CompileManager {

    @Getter
    private static final Collection<Class<? extends VariableDefinition<?>>> compileDataStructure = new ArrayList<>();

    @Getter
    @Setter
    private static CompileRuntimeFactory compileRuntimeFactory;

    static {
        compileRuntimeFactory = new DefaultCompileRuntimeFactory();
    }

    public static void register(Class<? extends VariableDefinition<?>> variableDefinition) {
        compileDataStructure.add(variableDefinition);
    }
}
