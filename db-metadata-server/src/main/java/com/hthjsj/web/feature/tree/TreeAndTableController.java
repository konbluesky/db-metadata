package com.hthjsj.web.feature.tree;

import com.google.common.base.Preconditions;
import com.hthjsj.analysis.component.ComponentType;
import com.hthjsj.analysis.meta.IMetaField;
import com.hthjsj.analysis.meta.IMetaObject;
import com.hthjsj.analysis.meta.MetaSqlKit;
import com.hthjsj.analysis.meta.aop.QueryPointCut;
import com.hthjsj.web.component.TableView;
import com.hthjsj.web.component.ViewFactory;
import com.hthjsj.web.component.form.FormView;
import com.hthjsj.web.controller.FrontRestController;
import com.hthjsj.web.jfinal.HttpRequestHolder;
import com.hthjsj.web.jfinal.SqlParaExt;
import com.hthjsj.web.kit.UtilKit;
import com.hthjsj.web.query.QueryConditionForMetaObject;
import com.hthjsj.web.query.QueryHelper;
import com.hthjsj.web.query.dynamic.CompileRuntime;
import com.hthjsj.web.ui.MetaObjectViewAdapter;
import com.hthjsj.web.ui.OptionsKit;
import com.hthjsj.web.ui.UIManager;
import com.jfinal.aop.Before;
import com.jfinal.kit.Kv;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p> @Date : 2020/9/10 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class TreeAndTableController extends FrontRestController {

    /**
     * 返回TreeAndTable 中meta数据
     * 1. 显式指定Tree组件的Data_Url
     * 2. 显式指定Table组件的DataUrl
     */
    public void meta() {
        QueryHelper queryHelper = new QueryHelper(this);
        String featureCode = queryHelper.getFeatureCode();

        TreeAndTableConfig treeAndTableConfig = featureService().loadFeatureConfig(featureCode);
        String tableObjectCode = treeAndTableConfig.getTableConfig().getObjectCode();

        MetaObjectViewAdapter metaObjectViewAdapter = UIManager.getView(metaService().findByCode(tableObjectCode), ComponentType.TABLEVIEW);
        TableView tableView = (TableView) metaObjectViewAdapter.getComponent();
        tableView.dataUrl("/f/tat/tableList?featureCode=" + featureCode);
        Kv tableMeta = tableView.toKv();
        Kv treeMeta = Kv.by("data_url", "/f/t?objectCode=" + treeAndTableConfig.getTreeConfig().getObjectCode() + "&featureCode=" + featureCode);

        renderJson(Ret.ok("data", Kv.create().set("table", tableMeta).set("tree", treeMeta)));
    }

    public void toAdd() {
        QueryHelper queryHelper = new QueryHelper(this);
        String featureCode = queryHelper.getFeatureCode();
        TreeAndTableConfig treeAndTableConfig = featureService().loadFeatureConfig(featureCode);

        /** 优先通过ForeignFieldCode取值 如无,再通过RELATE_ID_KEY  */
        String relateIdValue = getPara(treeAndTableConfig.getTableConfig().getForeignFieldCode(), getPara(TreeAndTableConfig.RELATE_ID_KEY, ""));
        Preconditions.checkArgument(StrKit.notBlank(relateIdValue), "树->表 关联ID[%s]丢失,请检查.", TreeAndTableConfig.RELATE_ID_KEY);

        /** 构建元对象与FormView */
        IMetaObject metaObject = metaService().findByCode(treeAndTableConfig.getTableConfig().getObjectCode());
        FormView formView = ViewFactory.formView(metaObject).action("/form/doAdd").addForm();

        /** 公共逻辑: 获取请求中已挂的参数 */
        Kv disableMetaFields = queryHelper.hasMetaParams(metaObject);
        /** 将关联RELATE_ID_KEY的value 获取到后,放入要disable的字段map中 */
        disableMetaFields.put(treeAndTableConfig.getTableConfig().getForeignFieldCode(), relateIdValue);
        if (!disableMetaFields.isEmpty()) {
            formView.buildChildren();
            disableMetaFields.forEach((key, value) -> {
                formView.getField(String.valueOf(key)).disabled(true).defaultVal(String.valueOf(value));
            });
        }

        renderJson(Ret.ok("data", formView.toKv()));
    }

    @Before(HttpRequestHolder.class)//OptionKit.trans->compileRuntime->需要从request中获取user对象;
    public void tableList() {
        /**
         * 1. query data by metaObject
         *  [x] 1.1 query all data paging
         *  [x] 1.2 query data by fields
         *  [x-] 1.3 allow some conditions
         * [x] 2. sort
         * [x] 3. set fields or excludes fields
         * [x] 4. paging
         * 5. escape fields value
         * 6. supported alias columns;
         */
        QueryHelper queryHelper = new QueryHelper(this);
        String featureCode = queryHelper.getFeatureCode();
        TreeAndTableConfig treeAndTableConfig = featureService().loadFeatureConfig(featureCode);

        Integer pageIndex = queryHelper.getPageIndex();
        Integer pageSize = queryHelper.getPageSize();
        String[] fields = queryHelper.list().fields();
        String[] excludeFields = queryHelper.list().excludeFields();

        IMetaObject metaObject = metaService().findByCode(treeAndTableConfig.getTableConfig().getObjectCode());

        Collection<IMetaField> filteredFields = UtilKit.filter(fields, excludeFields, metaObject.fields());
        QueryConditionForMetaObject queryConditionForMetaObject = new QueryConditionForMetaObject(metaObject, filteredFields);
        SqlParaExt sqlPara = queryConditionForMetaObject.resolve(getRequest().getParameterMap(), fields, excludeFields);
        /** 编译where后条件 */
        String compileWhere = new CompileRuntime().compile(metaObject.configParser().where(), getRequest());

        /** pointCut构建 */
        QueryPointCut queryPointCut = (QueryPointCut) treeAndTableConfig.getTreeFeatureIntercept().tableIntercept();
        TreeAndTableInvocation treeAndTableInvocation = new TreeAndTableInvocation(metaObject, this, queryHelper);
        treeAndTableInvocation.setSqlParaExt(sqlPara);
        treeAndTableInvocation.setCompileWhere(compileWhere);
        treeAndTableInvocation.setFilteredFields(filteredFields);
        treeAndTableInvocation.setTreeAndTableConfig(treeAndTableConfig);

        Page<Record> result = null;
        if (queryPointCut.prevent()) {
            result = queryPointCut.getResult(treeAndTableInvocation);
        } else {
            SqlParaExt pointCutSqlPara = (SqlParaExt) queryPointCut.queryWrapper(treeAndTableInvocation);
            /** 当拦截点未设置时,使用默认查询逻辑 */
            if (pointCutSqlPara == null) {
                result = metaService().paginate(pageIndex,
                                                pageSize,
                                                metaObject,
                                                sqlPara.getSelect(),
                                                MetaSqlKit.where(sqlPara.getSql(), compileWhere, metaObject.configParser().orderBy()),
                                                sqlPara.getPara());
            } else {
                /** 拦截器干预后的逻辑 */
                result = metaService().paginate(pageIndex, pageSize, metaObject, pointCutSqlPara.getSelect(), pointCutSqlPara.getFromWhere(), pointCutSqlPara.getPara());
            }
        }


        /**
         * escape field value;
         * 1. 是否需要转义的规则;
         */
        if (!queryHelper.list().raw()) {
            result.setList(OptionsKit.trans(filteredFields, result.getList()));
        }

        /**
         * 别名替换,参数中遇
         * a->b=123
         * c->d=123
         * 执行别名替换处理
         */
        Kv alias = Kv.create();
        AtomicBoolean hasAlias = new AtomicBoolean(false);
        UtilKit.toObjectFlat(getRequest().getParameterMap()).forEach((key, value) -> {
            if (key.contains("->")) {
                String[] ss = key.split("->");
                alias.set(ss[0], ss[1]);
                hasAlias.set(true);
            }
        });
        if (hasAlias.get()) {
            result.setList(UtilKit.aliasList(result.getList(), alias));
        }
        renderJsonExcludes(Ret.ok("data", result.getList()).set("page", toPage(result.getTotalRow(), result.getPageNumber(), result.getPageSize())), excludeFields);
    }
}