package com.atguigu.auth.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author l moonlight
 * @create 2023-03-18 19:57
 */
public interface SysroleService extends IService<SysRole> {

    Map<String,Object> findRoleDataByUserId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
