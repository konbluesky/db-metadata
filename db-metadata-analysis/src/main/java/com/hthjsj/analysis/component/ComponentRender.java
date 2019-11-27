package com.hthjsj.analysis.component;

import com.jfinal.kit.Kv;

/**
 * <p> @Date : 2019/11/27 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public interface ComponentRender<C> {

    C component();

    Kv render();
}
