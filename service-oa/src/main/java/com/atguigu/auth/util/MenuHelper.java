package com.atguigu.auth.util;

import com.atguigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author l moonlight
 * @create 2023-03-19 20:40
 */
public class MenuHelper {

    /**
     * 使用递归方法创建菜单
     * @param menuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> menuList) {
        //创建list集合，用于最终数据
        ArrayList<SysMenu> trees = new ArrayList<>();
        //把所有菜单数据进行遍历
        for (SysMenu sysMenu: menuList) {
            //递归入口进入
            //parentId=0是入口
            if (sysMenu.getParentId().longValue()==0){
                trees.add(getChildren(sysMenu,menuList));
            }
        }
        return trees;
    }

    private static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> menuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        //遍历所有菜单数据，判断id和parentid对应的关系
        for (SysMenu it: menuList){
            if (sysMenu.getId().longValue() == it.getParentId().longValue()){
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(it,menuList));
            }
        }
        return sysMenu;
    }
}
