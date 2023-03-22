package com.atguigu.auth;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;


@SpringBootTest
@Slf4j
public class ProcessTest {

    @Autowired
    private RepositoryService repositoryService; //做流程的

    @Autowired
    private RuntimeService runtimeService; //做流程实例的

    @Autowired
    private TaskService taskService; //做任务的

    @Autowired
    private HistoryService historyService;  //做历史记录的

    /*********************************************************************
     * 监听器
     */
    @Test
    public void deployProcess3(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban03.bpmn20.xml")
                .name("加班申请流程03")
                .deploy();
        log.info(deployment.getId());
        log.info(deployment.getName());
    }
    //启动流程的实例
    @Test
    public void startProcessInstance012(){
        ProcessInstance jiaban02 = runtimeService.startProcessInstanceByKey("jiaban03");
        log.info(jiaban02.getProcessDefinitionId());
        log.info(jiaban02.getId());
    }

    /*****************************************************************************
     * uel-method
     */
    @Test
    public void deployProcess2(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban01.bpmn20.xml")
                .addClasspathResource("process/jiaban01.png")
                .name("加班申请流程01")
                .deploy();
        log.info(deployment.getId());
        log.info(deployment.getName());
    }
    //启动流程的实例
    @Test
    public void startProcessInstance01(){
        ProcessInstance jiaban01 = runtimeService.startProcessInstanceByKey("jiaban01");
        log.info(jiaban01.getProcessDefinitionId());
        log.info(jiaban01.getId());
    }

    /*****************************************************************************
     * uel-value
     */
    //流程定义部署 deploy：部署
    @Test
    public void deployProcess1(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .addClasspathResource("process/jiaban.png")
                .name("加班申请流程")
                .deploy();
        log.info(deployment.getId());
        log.info(deployment.getName());

    }
    //启动流程的实例
    @Test
    public void startProcessInstance(){
        HashMap<String, Object> map = new HashMap<>();
        //设置任务人
        map.put("assignee1","lucy");
        map.put("assignee2","mary");
        ProcessInstance jiaban = runtimeService.startProcessInstanceByKey("jiaban", map);
        log.info(jiaban.getProcessDefinitionId());//流程实例id
        log.info(jiaban.getId());
    }


    //单个流程实例挂起
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId = "d4e07358-c7e9-11ed-a974-f4c88af6c80a";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        boolean suspended = processInstance.isSuspended(); //true是挂起状态 false是激活状态

        if (suspended){
            //激活
            runtimeService.activateProcessInstanceById(processInstanceId);
            log.info(processInstanceId+"激活了");
        }else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            log.info(processInstanceId+"挂起了   ");
        }
    }

    //全部挂起流程
    @Test
    public void suspendProcessInstanceAll(){
        //1 获取流程定义的对象
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("qingjia").singleResult();
        //2.调用流程定义对象的方法判断当前状态，：挂起，激活
        boolean suspended = qingjia.isSuspended();
        //3.判断如果挂起，实现激活 suspended true挂起 false：激活
        if (suspended){
            //第一个参数  流程定义的id
            //第二个参数  是否激活 true
            //第三个参数  时间点
            repositoryService.activateProcessDefinitionById(qingjia.getId(),true,null);
            log.info(qingjia.getId()+"激活了");
        }else {
            //如果激活，实现挂起
            //第二个参数  是否挂起 true
            repositoryService.suspendProcessDefinitionById(qingjia.getId(),true,null);
            log.info(qingjia.getId()+"挂起了");
        }
    }


    //创建流程实例，指定BusinessKey
    @Test
    public void startUpProcessAddBusinessKey(){
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("qingjia", "1001");
        log.info("业务key：{}",instance.getBusinessKey());
        log.info(instance.getId());
    }

    @Test
    public void findCompletTaskList(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("lisi")
                .finished().list();
        for (HistoricTaskInstance historicTaskInstance:list){
            log.info("实例id：{}",historicTaskInstance.getProcessInstanceId());
            log.info("任务id:" + historicTaskInstance.getId());
            log.info("任务负责人：" + historicTaskInstance.getAssignee());
            log.info("任务名称：" + historicTaskInstance.getName());
        }
    }

    //处理当前事务
    @Test
    public void completTask(){
        //查询负责人需要处理的任务，返回一条
        Task task = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .singleResult();
        //处理任务,参数是任务id
        taskService.complete(task.getId());
    }


    //查询个人的代办任务
    @Test
    public void testTaskList(){
        String assign = "jack";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task: list) {
            log.info("流程实例的id:"+task.getProcessInstanceId());
            log.info("任务id:" + task.getId());
            log.info("任务负责人：" + task.getAssignee());
            log.info("任务名称：" + task.getName());
        }
    }

    //启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia");

        log.info("流程定义id:{}"+processInstance.getProcessDefinitionId());
        log.info("流程实例id：{}"+processInstance.getProcessInstanceId());
        log.info("流程活动id：{}"+processInstance.getActivityId());

    }


    //单个项目部署
    @Test
    public void deployProcess() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    public void deployProcessByZip() throws IOException {
        // 定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "process/qingjia.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("请假申请流程")
                .deploy();
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
        inputStream.close();
        zipInputStream.close();
    }

}