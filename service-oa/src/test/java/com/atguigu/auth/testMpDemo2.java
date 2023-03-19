package com.atguigu.auth;

import com.atguigu.auth.service.SysroleService;
import com.atguigu.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author l moonlight
 * @create 2023-03-18 20:00
 */
@SpringBootTest
public class testMpDemo2 {
    @Autowired
    private SysroleService service;
    @Test
    private void  getAll(){
        List<SysRole> list = service.list();
        System.out.println(list);
    }

}
