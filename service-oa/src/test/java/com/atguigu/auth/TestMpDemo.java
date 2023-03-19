package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysroleService;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author l moonlight
 * @create 2023-03-18 19:22
 */
@SpringBootTest
public class TestMpDemo {
    //注入
    @Autowired
    private SysRoleMapper mapper;

    @Autowired
    private SysroleService service;

    //查询所有记录
    @Test
    public void getAll(){
        List<SysRole> sysRoles = mapper.selectList(null);
        System.out.println(sysRoles);
    }

    @Test
    public void testQuery1(){
        //QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        // wrapper.eq("role_name","系统管理员");
        //eq("role_name","系统管理员");eq中为数据库中表的字段名称
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName,"系统管理员");
        List<SysRole> roles = mapper.selectList(wrapper);
        System.out.println(roles);
    }

    //service查询
    @Test
    public void  getAll2(){
        List<SysRole> list = service.list();
        System.out.println(list);
    }

    //添加操作
    @Test
    public void  add(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role1");
        sysRole.setDescription("角色管理员");

        int insert = mapper.insert(sysRole);
        System.out.println(insert);
        System.out.println(sysRole.getId());
    }

    //修改操作
    @Test
    public void update(){
        //根据id查询
        SysRole sysRole = mapper.selectById(10);
        //设置修改职
        sysRole.setRoleName("小吕会出手");
        //调用方法实现最终修改
        int rows = mapper.updateById(sysRole);
        System.out.println(rows);
    }

    //删除
    @Test
    public void delete(){
        int rows = mapper.deleteById(10);
        System.out.println(rows);
    }

    //批量删除
    @Test
    public void testDeleteBatchIds(){
        int rows = mapper.deleteBatchIds(Arrays.asList(1, 2));
        System.out.println(rows);
    }



}
