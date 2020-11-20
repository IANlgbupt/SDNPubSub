package edu.bupt.wangfu.role.controller;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.device.Queue;
import edu.bupt.wangfu.info.device.Switch;
import edu.bupt.wangfu.info.device.User;
import edu.bupt.wangfu.info.message.admin.GroupMessage;
import edu.bupt.wangfu.module.queueMgr.QueueMgr;
import edu.bupt.wangfu.module.switchMgr.odl.OvsProcess;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topologyMgr.TopoMgr;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.module.util.store.GlobalSubPub;
import edu.bupt.wangfu.module.util.store.LocalSubPub;
import edu.bupt.wangfu.role.controller.listener.AdminListener;
import edu.bupt.wangfu.role.controller.listener.WsnListener;
import edu.bupt.wangfu.role.controller.util.ControllerInit;
import edu.bupt.wangfu.role.util.WsnTopicTask;
import edu.bupt.wangfu.test.PropertiesTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static edu.bupt.wangfu.module.queueMgr.util.GetInfo.getQueueInfo;

/**
 * 控制器入口程序，主要负责监听：
 *     1. adminMessage：管理消息，主题树下发
 *                      由 AdminListener 负责
 *     2. sysMessage：拓扑消息，拓扑构建、维护
 *                    由 TopoMgr 拓扑模块负责
 *     3. wsnMessage：wsn消息，集群发布订阅情况
 *                    由 WsnListener 监听本集群发布订阅情况
 *'
 * @see TopoMgr
 * @see AdminListener
 * @see WsnListener
 */
@Component
@Data
public class ControllerStart {
    //采用线程池管理线程的生命周期
    private static ExecutorService exec = Executors.newCachedThreadPool();

    @Autowired
    Controller controller;

    @Autowired
    private AdminListener adminListener;

    @Autowired
    private TopoMgr topoMgr;

    @Autowired
    private WsnListener wsnListener;

    @Autowired
    private ControllerInit controllerInit;

    //本地发布订阅信息
    @Autowired
    LocalSubPub localSubPub;

    //全局发布订阅信息
    @Autowired
    GlobalSubPub globalSubPub;

    @Autowired
    EncodeTopicTree encodeTopicTree;

    @Autowired
    WsnTopicTask wsnTopicTask;

    @Autowired
    OvsProcess ovsProcess;

    @Autowired
    QueueMgr queueMgr;

    public class GroupTask extends TimerTask{

        @Override
        public void run() {
            int adminPort = controller.getAdminPort();
            String address = controller.getAdminV6Addr();
            MultiHandler handler = new MultiHandler(adminPort, address);
            System.out.println("向管理员发送集群信息");
            GroupMessage msg = new GroupMessage();
            msg.setController(controller);
            msg.setGroupName(controller.getLocalGroupName());
            for (Switch swt : controller.getOutSwitches().values()) {
                for (String port : swt.getOutPorts().values()) {
                    String str = ovsProcess.dumpQueues(Integer.parseInt(port));
                    Map<Integer, List<Queue>> queueMap = getQueueInfo(str, Integer.parseInt(port));
                    swt.getQueueMap().putAll(queueMap);
                }
            }
            msg.setSwitchMap(controller.getSwitches());
            Set<String> subTopics = new HashSet<>();
            for (User user : localSubPub.getLocalSubMap().keySet()) {
                List<String> list = localSubPub.getLocalSubMap().get(user);
                for (String str : list) {
                    subTopics.add(str);
                }
            }
            msg.setSubTopics(subTopics);
            Set<String> pubTopics = new HashSet<>();
            for (User user : localSubPub.getLocalPubMap().keySet()) {
                List<String> list = localSubPub.getLocalPubMap().get(user);
                for (String str : list) {
                    pubTopics.add(str);
                }
            }
            msg.setPubTopics(pubTopics);
            handler.v6Send(msg);
        }
    }

    public void start() {
        //更新constant 类中的属性值
        PropertiesTest.refreshPro();
        //初始化集群，流表预下发
        controllerInit.init();
        //启动管理消息监听，接收主题树
        exec.execute(adminListener);
        //时间驱动，定时向wsn更新主题树
        new Timer().schedule(wsnTopicTask, 1000, 15000);
        //时间驱动，定时向管理员发送集群内信息
        new Timer().schedule(new GroupTask(), 1000, 20000);
        topoMgr.start();
        //启动wsn监听，接收集群内的发布订阅情况
        exec.execute(wsnListener);
        //initQueue();
        //启动队列管理
        queueMgr.start(exec);
    }
    //Add
    public void initQueue()
    {
        for (Switch swt : controller.getOutSwitches().values()) {
            for (String port : swt.getOutPorts().values()) {
                ovsProcess.defaultInitQueues(Integer.valueOf(port));
            }
        }
    }
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        ControllerStart controllerStart = (ControllerStart) context.getBean("controllerStart");
        controllerStart.start();
    }
}
