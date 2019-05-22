package edu.bupt.wangfu.test;

import edu.bupt.wangfu.config.ControllerConfig;
import edu.bupt.wangfu.info.message.wsn.TopicEncodeMsg;
import edu.bupt.wangfu.module.topicMgr.ldap.TopicUtil;
import edu.bupt.wangfu.module.topicTreeMgr.TopicTreeMgr;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.EncodeTopicTreeEntry;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTree;
import edu.bupt.wangfu.module.topicTreeMgr.topicTree.TopicTreeEntry;
import edu.bupt.wangfu.module.topicTreeMgr.util.EncodeUtil;
import edu.bupt.wangfu.module.util.MultiHandler;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static edu.bupt.wangfu.module.topicMgr.ldap.TopicUtil.getAllNodes;

@Component
public class Test {
    @Autowired
    private static TopicTree topicTree;

    @Autowired
    private static EncodeTopicTree encodeTopicTree;

    @Autowired
    private static TopicTreeMgr topicTreeMgr;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ControllerConfig.class);

        TopicEncodeMsg encodeMsg = new TopicEncodeMsg();
        topicTreeMgr.buildTopicTree();
        encodeMsg.setTopicTree(encodeTopicTree);
        System.out.println(encodeTopicTree);
        MultiHandler handler = new MultiHandler(30003, "FF0E:0000:0000:0000:0001:2345:6791:ABCD");
        handler.v6Send(encodeMsg);
    }

}
