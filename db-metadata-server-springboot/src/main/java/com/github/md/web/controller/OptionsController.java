package com.github.md.web.controller;

import com.github.md.analysis.meta.IMetaField;
import com.github.md.analysis.meta.IMetaObject;
import com.github.md.analysis.meta.MetaFieldConfigParse;
import com.github.md.web.WebException;
import com.github.md.web.query.QueryHelper;
import com.github.md.web.query.dynamic.CompileManager;
import com.github.md.web.ui.OptionsKit;
import com.github.md.analysis.kit.Kv;
import com.github.md.analysis.kit.Ret;
import com.jfinal.kit.StrKit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p> @Date : 2019/11/18 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@RestController
@RequestMapping("component/options")
public class OptionsController extends ControllerAdapter {

    /**
     * URL: /component/options/meta_object?f=field_code_abc
     */
    @GetMapping
    public Ret index() {
        QueryHelper queryHelper = queryHelper();
        String objectCode = queryHelper.getObjectCode();
        String fieldCode = queryHelper.getFieldCode();

        IMetaField metaField = metaService().findFieldByCode(objectCode, fieldCode);

        MetaFieldConfigParse metaFieldConfigParse = metaField.configParser();

        if (!metaFieldConfigParse.hasTranslation()) {
            throw new WebException("[%s]元对象的[%s]元字段未配置转义逻辑", objectCode, fieldCode);
        }
        if (metaFieldConfigParse.isOptions()) {
            return Ret.ok("data", metaFieldConfigParse.options());
        }
        if (metaFieldConfigParse.isSql()) {
            IMetaObject metaObject = metaField.getParent();
            String dbConfig = StrKit.defaultIfBlank(metaFieldConfigParse.dbConfig(), metaObject.schemaName());
            String compileSql = CompileManager.getCompileRuntimeFactory().createCompileRuntime().compile(metaFieldConfigParse.scopeSql(), getRequest());
            List<Kv> options = OptionsKit.transKeyValueBySql(compileSql, dbConfig);
            return Ret.ok("data", options);
        }

        return Ret.ok();
    }
}
