package com.hthjsj.analysis.meta;

import com.alibaba.fastjson.JSON;
import com.hthjsj.analysis.meta.aop.*;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> @Date : 2019/11/14 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
@Slf4j
public class MetaObjectConfigParse extends MetaData {

    MetaObjectConfigParse(Kv config) {
        set(config);
    }

    MetaObjectConfigParse(String config) {
        set(JSON.parseObject(config));
    }

    public boolean isUUIDPrimary() {
        return Boolean.parseBoolean(getStr("isUUIDPrimary"));
    }

    public <T extends IPointCut> T interceptor() {
        if (!StrKit.notBlank(getStr("bizInterceptor"))) {
            return (T) new PointCut();
        }

        T clazz = null;
        try {
            clazz = (T) Class.forName(getStr("bizInterceptor")).newInstance();
            return clazz;
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (NullPointerException e) {
            log.error("元对象 {},未配置拦截器.", getStr("objectCode"));
        }
        log.warn("元对象{},使用默认拦截器{}", getStr("objectCode"), PointCut.class);
        return (T) new PointCut();
    }

    /**
     * 客户端可以按需选择实现add,update,delete,view切入点
     */
    public DeletePointCut deletePointCut() {
        Object o = interceptor();
        if (o instanceof DeletePointCut) {
            return (DeletePointCut) o;
        }
        return new PointCut();
    }

    public AddPointCut addPointCut() {
        Object o = interceptor();
        if (o instanceof AddPointCut) {
            return (AddPointCut) o;
        }
        return new PointCut();
    }

    public UpdatePointCut updatePointCut() {
        Object o = interceptor();
        if (o instanceof UpdatePointCut) {
            return (UpdatePointCut) o;
        }
        return new PointCut();
    }

    public ViewPointCut viewPointCut() {
        Object o = interceptor();
        if (o instanceof ViewPointCut) {
            return (ViewPointCut) o;
        }
        return new PointCut();
    }
}
