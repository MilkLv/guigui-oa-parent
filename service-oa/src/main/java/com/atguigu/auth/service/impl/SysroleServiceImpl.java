package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.auth.service.SysroleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author l moonlight
 * @create 2023-03-18 19:58
 */
@Service
public class SysroleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysroleService {
    @Autowired
    private SysUserRoleService userRoleService;
    /**
     * 1.查询所有角色 和当前用户所属角色
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        //1.查询到所有的角色，返回list集合
        List<SysRole> allRoleList = baseMapper.selectList(null);
        //2.根据userId查询角色用户关系表，查询userid对应所有角色id
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,userId);
        List<SysUserRole> existRoleList = userRoleService.list(queryWrapper);
//        ArrayList<Long> list = new ArrayList<>();
//        for (SysUserRole sysUserRole  : existRoleList) {
//            Long roleId = sysUserRole.getRoleId();
//            list.add(roleId);
//        }
        List<Long> existRoleIdList = existRoleList.stream()
                .map(item -> item.getRoleId()).collect(Collectors.toList());

        //3.根据查询所有角色id，找到对应的角色信息
        //根据角色id到所有的角色list集合进行比较
        List<SysRole> assignRolelist = new ArrayList<>();
        for (SysRole sysRole :allRoleList){
            //比较
            if (existRoleIdList.contains(sysRole.getId())){
                assignRolelist.add(sysRole);
            }
        }
        //4把得到两部分数据封装map集合，返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRolelist", assignRolelist);
        roleMap.put("allRoleList", allRoleList);
        return roleMap;
    }

    /**
     * 2.为用户分配角色
     * @param assginRoleVo
     */
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //把用户之前分哦欸角色数据删除，用户角色关系表里面，根据userid删除
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        userRoleService.remove(queryWrapper);
        //重新进行分配
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId:roleIdList){
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(assginRoleVo.getUserId());
            userRole.setRoleId(roleId);
            userRoleService.save(userRole);
        }
    }
}
