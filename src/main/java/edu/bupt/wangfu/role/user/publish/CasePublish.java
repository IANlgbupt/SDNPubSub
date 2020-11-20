package edu.bupt.wangfu.role.user.publish;/**
 * Created by IntelliJ IDEA.
 * User: 15373
 * Date: 2020/4/1
 */

/**
 * Created by-logan on 2020/4/1
 */
public class CasePublish {
    private Trans trans;
    public static String wsnAddr = "http://127.0.0.1:9011/wsn-core";
    public static String sendAddr = "http://127.0.0.1:9013/wsn-send";
    public CasePublish(String sendAddr,String wsnAddr,String topic)
    {
        trans = new Trans(sendAddr,wsnAddr,topic);
    }
    public void register()
    {
        trans.register();
    }
    public void publishmsg(String msg)
    {
        trans.sendMethod(msg);
    }

    public static void main(String[] args) {
        CasePublish pub = new CasePublish(wsnAddr,sendAddr,"test");
        pub.register();
        pub.publishmsg("hello world");
    }
}
