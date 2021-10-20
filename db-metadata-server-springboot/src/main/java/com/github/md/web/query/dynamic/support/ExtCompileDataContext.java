package com.github.md.web.query.dynamic.support;

import com.github.md.web.query.dynamic.CompileManager;
import com.github.md.web.query.dynamic.Context;
import com.github.md.web.query.dynamic.VariableDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> @Date : 2020/8/12 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class ExtCompileDataContext implements Context {

    @Override
    public Map data() {
        Map data = new HashMap();
        for (Class<? extends VariableDefinition<?>> variableDefinitionClass : CompileManager.getCompileDataStructure()) {
            try {
                VariableDefinition instance = variableDefinitionClass.newInstance();
                data.put(instance.name(), instance.getValue());
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
        return data;
    }
}
