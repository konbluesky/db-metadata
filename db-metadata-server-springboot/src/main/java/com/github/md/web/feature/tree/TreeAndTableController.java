package com.github.md.web.feature.tree;

import com.github.md.analysis.meta.*;
import com.github.md.web.component.SearchView;
import com.github.md.web.component.TableView;
import com.github.md.web.component.ViewFactory;
import com.github.md.web.query.QueryBuilder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.github.md.analysis.component.ComponentType;
import com.github.md.analysis.meta.aop.AddPointCut;
import com.github.md.analysis.meta.aop.AopInvocation;
import com.github.md.analysis.meta.aop.PointCutChain;
import com.github.md.analysis.meta.aop.QueryPointCut;
import com.github.md.web.component.form.FormView;
import com.github.md.web.controller.ControllerAdapter;
import com.github.md.web.controller.ParameterHelper;
import com.github.md.web.kit.SqlParaExt;
import com.github.md.web.jms.EventKit;
import com.github.md.web.jms.FormMessage;
import com.github.md.web.kit.UtilKit;
import com.github.md.web.query.FormDataFactory;
import com.github.md.web.query.QueryConditionForMetaObject;
import com.github.md.web.query.QueryHelper;
import com.github.md.web.query.dynamic.CompileRuntime;
import com.github.md.web.ui.MetaObjectViewAdapter;
import com.github.md.web.ui.OptionsKit;
import com.github.md.web.ui.UIManager;
import com.github.md.analysis.kit.Kv;
import com.github.md.analysis.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p> @Date : 2020/9/10 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
@RestController
@RequestMapping(value = { "f/tat", "feature/treeAndTable" })
public class TreeAndTableController extends ControllerAdapter {

    /**
     * ??????TreeAndTable ???meta??????
     * 1. ????????????Tree?????????Data_Url
     * 2. ????????????Table?????????DataUrl
     */
    @GetMapping("meta")
    public Ret meta() {
        QueryHelper queryHelper = queryHelper();
        String featureCode = queryHelper.getFeatureCode();

        TreeAndTableConfig treeAndTableConfig = featureService().loadFeatureConfig(featureCode);
        String tableObjectCode = treeAndTableConfig.getTableConfig().getObjectCode();

        IMetaObject metaObject = metaService().findByCode(tableObjectCode);
        MetaObjectViewAdapter metaObjectTableViewAdapter = UIManager.getView(metaObject, ComponentType.TABLEVIEW);
        TableView tableView = (TableView) metaObjectTableViewAdapter.getComponent();
        tableView.dataUrl("/f/tat/tableList?featureCode=" + featureCode);

        Kv treeMeta = Kv.by("data_url", "/f/t?objectCode=" + treeAndTableConfig.getTreeConfig().getObjectCode() + "&featureCode=" + featureCode);

        MetaObjectViewAdapter metaObjectSearchViewAdapter = UIManager.getView(metaObject, ComponentType.SEARCHVIEW);
        SearchView searchView = (SearchView) metaObjectSearchViewAdapter.getComponent();

        return (Ret.ok("data", Kv.create().set("table", tableView.toKv()).set("tree", treeMeta).set("search", searchView.toKv())));
    }

    @GetMapping("toAdd")
    public Ret toAdd() {
        QueryHelper queryHelper = queryHelper();
        ParameterHelper parameterHelper = parameterHelper();
        String featureCode = queryHelper.getFeatureCode();
        TreeAndTableConfig treeAndTableConfig = featureService().loadFeatureConfig(featureCode);

        String foreignFieldCodeKey = treeAndTableConfig.getTableConfig().getForeignFieldCode();
        /** ????????????ForeignFieldCodeKey?????? ??????,?????????RELATE_ID_KEY  */
        String relateIdValue = parameterHelper.getPara(foreignFieldCodeKey, parameterHelper.getPara(TreeAndTableConfig.RELATE_ID_KEY, ""));
        Preconditions.checkArgument(StrKit.notBlank(relateIdValue), "???->??? ??????ID[%s]??????,?????????.", TreeAndTableConfig.RELATE_ID_KEY);

        /** ??????Table????????? */
        IMetaObject metaObject = metaService().findByCode(treeAndTableConfig.getTableConfig().getObjectCode());

        /**
         * ????????????foreignFieldCodeKey,?????????????????????????????????????????????????????????
         * RELATE_ID_KEY????????????????????????,???????????????????????????????????????
         * */
        if (StrKit.isBlank(foreignFieldCodeKey)) {
            IMetaField virtualField = MetaFactory.createReadonlyMetaField(metaObject, TreeAndTableConfig.RELATE_ID_KEY, "????????????", relateIdValue);
            metaObject.addField(virtualField);
        }

        QueryBuilder queryBuilder = queryHelper.queryBuilder();
        queryBuilder.builder("featureCode", featureCode);

        FormView formView = ViewFactory.formView(metaObject).action("/f/tat/doAdd" + queryBuilder.buildQueryString(true)).addForm();
        /** ????????????: ?????????????????????????????? */
        Kv disableMetaFields = queryHelper.hasMetaParams(metaObject);

        /** ?????????RELATE_ID_KEY???value ????????????,?????????disable?????????map??? */
        if (StrKit.isBlank(foreignFieldCodeKey)) {
            disableMetaFields.put(TreeAndTableConfig.RELATE_ID_KEY, relateIdValue);
        } else {
            disableMetaFields.put(foreignFieldCodeKey, relateIdValue);
        }

        if (!disableMetaFields.isEmpty()) {
            formView.buildChildren();
            disableMetaFields.forEach((key, value) -> {
                formView.getField(String.valueOf(key)).disabled(true).defaultVal(String.valueOf(value));
            });
        }

        return Ret.ok("data", formView.toKv());
    }

    @PostMapping("doAdd")
    public Ret doAdd() {
        QueryHelper queryHelper = queryHelper();
        ParameterHelper parameterHelper = parameterHelper();
        String featureCode = queryHelper.getFeatureCode();
        TreeAndTableConfig treeAndTableConfig = featureService().loadFeatureConfig(featureCode);

        IMetaObject metaObject = metaService().findByCode(treeAndTableConfig.getTableConfig().getObjectCode());

        MetaData metadata = FormDataFactory.buildFormData(getRequest().getParameterMap(), metaObject, true);

        MetaObjectConfigParse metaObjectConfigParse = metaObject.configParser();
        AddPointCut addPointCut = (AddPointCut) treeAndTableConfig.getTreeFeatureIntercept().tableIntercept();
        /** ???TreeAndTable???????????????????????????AddPointCut???????????? */
        AddPointCut[] pointCut = Lists.asList(addPointCut, metaObjectConfigParse.addPointCut()).toArray(new AddPointCut[0]);
        AopInvocation invocation = new AopInvocation(metaObject, metadata, parameterHelper.getKv());

        boolean status = Db.tx(new IAtom() {

            @Override
            public boolean run() throws SQLException {
                boolean s = false;
                try {
                    PointCutChain.addBefore(pointCut, invocation);
                    s = metaService().saveData(invocation.getMetaObject(), invocation.getFormData());
                    invocation.setPreOperateStatus(s);
                    PointCutChain.addAfter(pointCut, invocation);
                } catch (Exception e) {
                    log.error("????????????\n?????????:{},????????????:{}", metaObject.code(), e.getMessage());
                    log.error(e.getMessage(), e);
                    s = false;
                }
                return s;
            }
        });

        EventKit.post(FormMessage.AddMessage(invocation));

        return (status ? Ret.ok() : Ret.fail());
    }

    @GetMapping("tableList")
    public Object tableList() {
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
        QueryHelper queryHelper = queryHelper();

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
        /** ??????where????????? */
        String compileWhere = new CompileRuntime().compile(metaObject.configParser().where(), getRequest());

        /** pointCut?????? */
        QueryPointCut queryPointCut = (QueryPointCut) treeAndTableConfig.getTreeFeatureIntercept().tableIntercept();
        TreeAndTableInvocation treeAndTableInvocation = new TreeAndTableInvocation(metaObject, queryHelper);
        treeAndTableInvocation.setSqlParaExt(sqlPara);
        treeAndTableInvocation.setCompileWhere(compileWhere);
        treeAndTableInvocation.setFilteredFields(filteredFields);
        treeAndTableInvocation.setTreeAndTableConfig(treeAndTableConfig);

        Page<Record> result = null;
        if (queryPointCut.prevent()) {
            result = queryPointCut.getResult(treeAndTableInvocation);
        } else {
            SqlParaExt pointCutSqlPara = (SqlParaExt) queryPointCut.queryWrapper(treeAndTableInvocation);
            /** ????????????????????????,???????????????????????? */
            if (pointCutSqlPara == null) {
                result = metaService().paginate(pageIndex,
                                                pageSize,
                                                metaObject,
                                                sqlPara.getSelect(),
                                                MetaSqlKit.where(sqlPara.getSql(), compileWhere, metaObject.configParser().orderBy()),
                                                sqlPara.getPara());
            } else {
                /** ??????????????????????????? */
                result = metaService().paginate(pageIndex, pageSize, metaObject, pointCutSqlPara.getSelect(),
                                                //                        pointCutSqlPara.getFromWhere(),
                                                MetaSqlKit.where(pointCutSqlPara.getSql(), compileWhere, metaObject.configParser().orderBy()), pointCutSqlPara.getPara());
            }
        }


        /**
         * escape field value;
         * 1. ???????????????????????????;
         */
        if (!queryHelper.list().raw()) {
            result.setList(OptionsKit.trans(filteredFields, result.getList()));
        }

        /**
         * ????????????,????????????
         * a->b=123
         * c->d=123
         * ????????????????????????
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
        return renderJsonExcludes(Ret.ok("data", result.getList()).set("page", toPage(result.getTotalRow(), result.getPageNumber(), result.getPageSize())),
                                  excludeFields);
    }
}
