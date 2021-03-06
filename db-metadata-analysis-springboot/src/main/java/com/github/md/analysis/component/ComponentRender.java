package com.github.md.analysis.component;

import com.github.md.analysis.kit.Kv;

/**
 * 组件渲染工具
 * <p> @Date : 2019/11/27 </p>
 * <p> @Project : db-meta-serve</p>
 *
 * <p> @author konbluesky </p>
 */
public interface ComponentRender<C> {

    C component();

    Kv render();
}
