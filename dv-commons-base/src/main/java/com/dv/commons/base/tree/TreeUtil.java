package com.dv.commons.base.tree;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * 树工具类
 */
public class TreeUtil{


    /**
     * 集合转树结构,注意,使用此方法,则集合元素必须继承ITree接口
     * @param collection 目标集合
     * @return
     */
    public static <ID,T extends ITree<ID>> Collection<T> toTree(@NonNull Collection<T> collection){
        try {
            // 找出所有的根节点
            Collection<T> roots = null;
            if (collection.getClass().isAssignableFrom(Set.class)) roots = new HashSet<>();
            else roots = new ArrayList<>();
            for (T tree : collection) {
                ID o = tree.getParent();
                if (o instanceof String s){
                    if (isEmpty(s)){
                        roots.add(tree);
                    }
                } else if (o == null){
                    roots.add(tree);
                }
            }
            // 从目标集合移除所有的根节点
            collection.removeAll(roots);
            // 为根节点添加孩子节点
            for (T tree:roots){
                addChild(tree,collection);
            }
            return roots;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static boolean isEmpty(String s){
        return s == null || s.trim().length() == 0;
    }

    public static <ID,T extends ITree<ID>> void addChild(T tree,Collection<T> collection){
        try {
            Object id = tree.getId();
            Collection<ITree<ID>> children = tree.getChildren();
            for (T cc:collection){
                ID o = cc.getParent();
                if (id.equals(o)){// 如果当前节点的被依赖值和目标节点的被依赖值相等,则说明,当前节点是目标节点的子节点
                    if (children==null) {// 如果目标节点的孩子集合为null,初始化目标节点的孩子集合
                        if (collection.getClass().isAssignableFrom(Set.class)){// 如果目标集合是一个set集合,则初始化目标节点的孩子节点集合为set
                            children = new HashSet<>();
                        }else children = new ArrayList<>();// 否则初始化为list
                    }
                    // 将当前节点添加到目标节点的孩子节点
                    children.add(cc);
                    // 重设目标节点的孩子节点集合,这里必须重设,因为如果目标节点的孩子节点是null的话,这样是没有地址的,就会造成数据丢失,所以必须重设,如果目标节点所在类的孩子节点初始化为一个空集合,而不是null,则可以不需要这一步,因为java一切皆指针
                    tree.setChildren(children);
                    // 递归添加孩子节点
                    addChild(cc,collection);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}