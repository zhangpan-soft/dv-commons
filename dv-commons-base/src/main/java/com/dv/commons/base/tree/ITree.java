package com.dv.commons.base.tree;

import java.util.Collection;

public interface ITree<ID>{
    /**
     * 获取被依赖节点
     * @return
     */
    ID getId();

    /**
     * 获取依赖节点
     * @return
     */
    ID getParent();


    /**
     * 获取孩子列表
     * @return
     */
    <T extends ITree<ID>>Collection<T> getChildren();

    /**
     * 设置孩子列表
     * @param children
     */
    <T extends ITree<ID>>void setChildren(Collection<T> children);
}
