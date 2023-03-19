package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysMenuMapper;
import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysRoleMenuService;
import com.atguigu.auth.util.MenuHelper;
import com.atguigu.common.config.exception.XiaoException;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xiaolv
 * @since 2023-03-19
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService roleMenuService;

    /**
     菜单列表接口
     */
    @Override
    public List<SysMenu> findNodes() {
        //查询所有的菜单数据
        List<SysMenu> menuList = baseMapper.selectList(null);

        List<SysMenu> resultList=  MenuHelper.buildTree(menuList);
        return resultList;
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void removeMenuById(Long id) {
        //判断当前菜单是否有下一层下单
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId,id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count>0){
            throw new XiaoException(201,"菜单有子菜单，不能删除");
        }
        baseMapper.deleteById(id);
    }

    /**
     * 查询所有的菜单和角色分配的菜单
     */
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        //1.查询所有条件-添加条件 status=1
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> sysMenuList = baseMapper.selectList(wrapper);
        //2.根据角色id roleId查询 角色菜单关系表里面 角色id对应所有的菜单id
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<SysRoleMenu> sysRoleMenuList = roleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        //3.根据获取菜单的id，获取对应菜单对象
        List<Long> menuList = sysRoleMenuList.stream()
                .map(item -> item.getMenuId()).collect(Collectors.toList());

        //3.1拿着菜单id 和所有集合里的菜单进行比较，如果相同封装
        sysMenuList.stream().forEach(item ->{
            if (menuList.contains(item.getId())){
                item.setSelect(true);
            }else{
                item.setSelect(false);
            }
        });
        //返回规定格式的菜单列表
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        return sysMenus;
    }

    /**
     * 为角色分配菜单
     */
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {
        //1.根据角色id删除菜单角色表分配数据
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId,assginMenuVo.getRoleId());
        roleMenuService.remove(queryWrapper);
        //2.从参数里面获取角色新分配菜单id列表
        //进行遍历，把每个id数据添加菜单角色表
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for (Long menuId:menuIdList) {
            if (StringUtils.isEmpty(menuId)){
                //跳出当前循环
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            roleMenuService.save(sysRoleMenu);
        }
    }
}
