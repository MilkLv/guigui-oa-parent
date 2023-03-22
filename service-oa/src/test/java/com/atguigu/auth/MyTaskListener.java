package com.atguigu.auth;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author l moonlight
 * @create 2023-03-21 22:24
 */
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask task) {
        if (task.getName().equals("经理审批")){
            //分配任务
            task.setAssignee("jack");
        }else if (task.getName().equals("人事审批")){
            task.setAssignee("jijiao");
        }
    }
}
