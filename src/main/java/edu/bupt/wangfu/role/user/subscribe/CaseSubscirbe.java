package edu.bupt.wangfu.role.user.subscribe;/**
 * Created by IntelliJ IDEA.
 * User: 15373
 * Date: 2020/4/1
 */

import edu.bupt.wangfu.role.user.publish.CasePublish;

/**
 * Created by-logan on 2020/4/1.
 */
public class CaseSubscirbe {
    private Trans trans;

    public static String wsnAddr = "http://127.0.0.1:9011/wsn-core";
    public static String sendAddr = "http://127.0.0.1:9008/wsn-subscribe";
    public CaseSubscirbe(String sendAddr,String wsnAddr,String topic)
    {
        trans = new Trans(sendAddr,wsnAddr,topic);
    }
    public void subscibe()
    {
        trans.subscribe();
    }


    public static void main(String[] args) {
        CaseSubscirbe sub = new CaseSubscirbe(sendAddr,wsnAddr,"test");
        sub.subscibe();
    }
}
