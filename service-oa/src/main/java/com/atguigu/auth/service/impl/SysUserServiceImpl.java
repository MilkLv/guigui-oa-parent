package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiaolv
 * @since 2023-03-19
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 根据id修改用户状态
     * @param id
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(Long id, Integer status) {
        //根据userId查询用户对象
        SysUser sysUser = baseMapper.selectById(id);
        //设置修改状态值
        //sysUser.setStatus(status);
        if(status.intValue() == 1) {
            sysUser.setStatus(status);
        } else {
            sysUser.setStatus(0);
        }
        //调用方法进行修改
        baseMapper.updateById(sysUser);
    }
}
