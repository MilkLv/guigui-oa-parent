package com.atguigu.auth.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author xiaolv
 * @since 2023-03-19
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
    菜单列表接口
     */
    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    /**
     * 查询所有的菜单和角色分配的菜单
     */
    List<SysMenu> findMenuByRoleId(Long roleId);

    /**
     * 为角色分配菜单
     */
    void doAssign(AssginMenuVo assginMenuVo);

    List<RouterVo> findUserMenuListByUserId(Long userId);


    List<String> findUserPermsByUserId(Long userId);
}
