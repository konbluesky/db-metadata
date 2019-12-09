package com.hthjsj.web.module.ms;

import com.google.common.collect.Maps;
import com.hthjsj.analysis.component.ComponentType;
import com.hthjsj.analysis.meta.MetaData;
import com.hthjsj.web.module.Module;
import com.jfinal.kit.Kv;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p> @Date : 2019/12/9 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public class MasterSlaveConfig extends MetaData implements Module {

    Master master;

    List<Slave> slaves;

    public String masterCode() {
        return master.getObjectCode();
    }

    public String masterKey() {
        return master.getPrimaryKey();
    }

    public Slave get(int i) {
        return slaves.get(i);
    }

    @Override
    public List<String> metaObjects() {
        List<String> objectCodes = slaves.stream().map(Slave::getObjectCode).collect(Collectors.toList());
        objectCodes.add(master.getObjectCode());
        return objectCodes;
    }

    @Override
    public Map<String, ComponentType> componentsMap() {
        Map<String, ComponentType> values = Maps.newHashMap();
        metaObjects().forEach(s -> {
            values.put(s, ComponentType.TABLEVIEW);
        });
        return values;
    }

    @Override
    public Kv execute() {
        return this;
    }

    @Data
    class Master {

        String objectCode;

        String primaryKey;
    }

    @Data
    class Slave {

        String objectCode;

        String foreignFieldCode;

        int order;
    }
}
