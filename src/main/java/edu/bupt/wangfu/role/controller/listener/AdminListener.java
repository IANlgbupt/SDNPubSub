package edu.bupt.wangfu.role.controller.listener;

import edu.bupt.wangfu.info.device.Controller;
import edu.bupt.wangfu.info.message.admin.AdminMessage;
import edu.bupt.wangfu.info.message.admin.EncodeTopicTreeMsg;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.util.MultiHandler;
import edu.bupt.wangfu.role.controller.ControllerStart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AdminListener implements Runnable{

    @Autowired
    Controller controller;

    @Autowired
    @Lazy
    ControllerStart controllerStart;

    @Override
    public void run() {
        System.out.println("控制器 admin 监听启动~~");
        int adminPort = controller.getAdminPort();
        String address = controller.getAdminV6Addr();
        MultiHandler handler = new MultiHandler(adminPort, address);
        while (true) {
            Object msg = handler.v6Receive();
            onMsgReceive(msg);
        }
    }

    /**
     * 收到消息后的处理措施：
     *     1. 更新本地主题编码树
     *     2. 向wsn发送主题树
     * @param msg
     */
    private void onMsgReceive(Object msg) {
        if (msg instanceof AdminMessage) {
            if (msg instanceof EncodeTopicTreeMsg) {
                System.out.println("admin message");
                EncodeTopicTree encodeTopicTree = ((EncodeTopicTreeMsg) msg).getEncodeTopicTree();
                controllerStart.setEncodeTopicTree(encodeTopicTree);

                //事件驱动，发送接收到的主题树编码
                int wsnPort = controller.getWsnPort();
                String address = controller.getWsnV6Addr();
                MultiHandler handler = new MultiHandler(wsnPort, address);
                handler.v6Send(encodeTopicTree);
            }
        }
    }
}
