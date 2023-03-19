package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysroleService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.AssginRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author l moonlight
 * @create 2023-03-18 20:11
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysroleService sysroleService;

    /**
     * 1.查询所有角色 和当前用户所属角色
     * @param userId
     * @return
     */
    @ApiOperation("获取角色")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
       Map<String,Object> map = sysroleService.findRoleDataByUserId(userId);
       return Result.ok(map);
    }

    /**
     * 2.为用户分配角色
     * @param assginRoleVo
     * @return
     */
    @ApiOperation("根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysroleService.doAssign(assginRoleVo);
        return Result.ok();
    }
    /**
     * 查询所有角色
     */
    @ApiOperation("查询所有角色")
    @GetMapping("findAll")
    public Result findAll(){
        List<SysRole> list = sysroleService.list();
        return Result.ok(list);
    }

    /**
     * 条件分页查询
     * @param page 当前页
     * @param limit 每页显示记录数
     * @param queryVo 条件对象
     * @return
     */
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo queryVo){
        //调用service的方法实现
        //1.创建page对象，传递分页相关参数
        Page<SysRole> pageParam = new Page<>(page, limit);
        //2.封装条件，判断条件是否为空，不空则进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = queryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)){
            //封装
            wrapper.like(SysRole::getRoleName,roleName);
        }
        //3.调用方法实现
        IPage<SysRole> pageModel = sysroleService.page(pageParam,wrapper);
        return Result.ok(pageModel);
    }

    /**
     * 添加角色
     * @param sysRole
     * @return
     */
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole){
        boolean is_success = sysroleService.save(sysRole);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 修改角色-根据id查询
     * @param id
     * @return
     */
    @ApiOperation("根据id查询")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id") Long id){
        SysRole sysRole = sysroleService.getById(id);
        return Result.ok(sysRole);
    }

    /**
     * 修改角色-最终修改
     * @param sysRole
     * @return
     */
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole){

        boolean is_success = sysroleService.updateById(sysRole);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @ApiOperation("删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") Long id){
        boolean is_success = sysroleService.removeById(id);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }

    /**
     * 批量删除
     * @return
     */
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> ids){
        boolean is_success = sysroleService.removeByIds(ids);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }


}
