package com.github.md.web.query.sqls;

import com.google.common.base.Splitter;
import com.github.md.analysis.meta.IMetaField;
import com.github.md.analysis.meta.MetaSqlKit;
import com.github.md.web.kit.UtilKit;
import com.jfinal.kit.StrKit;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> @Date : 2019/11/22 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class InNotInMatch extends MetaSQLExtract {

    public static final String IN_SUFFIX = "_in";

    public static final String NOT_IN_SUFFIX = "_nin";

    Map<String, Object> values = new HashMap<>();

    @Override
    public void init(IMetaField metaField, Map<String, Object> httpParams) {
        String inValString = UtilKit.defaultIfBlank(String.valueOf(httpParams.get(metaField.fieldCode() + IN_SUFFIX)), "");
        String[] inValues = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(inValString).toArray(new String[] {});

        String notInValString = UtilKit.defaultIfBlank(String.valueOf(httpParams.get(metaField.fieldCode() + NOT_IN_SUFFIX)), "");
        String[] notInValues = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(notInValString).toArray(new String[] {});

        String fieldCode = MetaSqlKit.discernColumns(metaField.fieldCode());
        if (metaField.configParser().isMultiple()) {
            // TODO 额外处理 多值逻辑,虽然入口是通过 in/nin 进来,但是需要用locate生成sql
            // 逻辑混入,这种兼容逻辑应放在XXXMatch以外的地方
            if (inValues.length > 0) {
                //locate('1',column_name)
                //((locate('1',column_name)>0 or locate('2',column_name)>0))
                String[] vs = new String[inValues.length];
                for (int i = 0; i < inValues.length; i++) {
                    vs[i] = (" LOCATE('" + inValues[i] + "'," + fieldCode + ")>0 ");
                }
                log.info("InNotInMatch 处理多值逻辑,{}-{}; value:{}", metaField.objectCode(), fieldCode, Arrays.toString(inValues));
                log.info("处理后数组数据:{}", Arrays.toString(vs));
                log.info("处理后SQL:{}", "(" + StrKit.join(vs, " or ") + ")");
                values.put(SQL_PREFIX + "(" + StrKit.join(vs, " or ") + ")", null);
            }
        } else {
            if (notInValues.length > 0) {
                // sqlKey : "fieldAbc not in(?,?,?)"  value:["s1","s2","s3"]
                values.put(toSqlKey(fieldCode, notInValues, false), notInValues);
            }
            if (inValues.length > 0) {
                // sqlKey : "fieldAbc in(?,?,?)"  value:["s1","s2","s3"]
                values.put(toSqlKey(fieldCode, inValues, true), inValues);
            }
        }
    }

    public String toSqlKey(String fieldCode, Object[] values, boolean isIn) {
        StringBuilder sb = new StringBuilder();
        for (Object v : values) {
            if (StrKit.notBlank(String.valueOf(v))) {
                sb.append(",?");
            }
        }
        String s = sb.toString().replaceFirst("(,)", "");
        return fieldCode + (isIn ? " in" : " not in") + "(" + s + ") ";
    }

    @Override
    public Map<String, Object> result() {
        return values;
    }
}
