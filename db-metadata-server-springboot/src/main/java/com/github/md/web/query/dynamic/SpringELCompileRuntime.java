package com.github.md.web.query.dynamic;

import com.github.md.web.config.NotFinishException;
import com.jfinal.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> @Date : 2021/10/19 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class SpringELCompileRuntime implements CompileRuntime {

    @Override
    public String compile(String sqlTemplate) {
        EvaluationContext elContext = new SimpleEvaluationContext.Builder().build();
        Context context = new ExtCompileDataContext();
        context.data().forEach((k, v) -> {
            elContext.setVariable((String) k, v);
        });
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression parseExpression = spelExpressionParser.parseExpression(sqlTemplate);
        Object result = parseExpression.getValue(context);
        log.info("Compile sqlTemplate:{}", result);
        return (String) result;
    }

    @Override
    public String compile(String sqlTemplate, Context context) {
        throw new NotFinishException("Not yet finished!");
    }

    @Override
    public String compile(String sqlTemplate, HttpServletRequest request) {
        throw new NotFinishException("Not yet finished!");
    }

    @Override
    public String compile(String sqlTemplate, HttpServletRequest request, Context context) {
        throw new NotFinishException("Not yet finished!");
    }
}
