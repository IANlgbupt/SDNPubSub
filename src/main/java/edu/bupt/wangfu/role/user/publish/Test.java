package edu.bupt.wangfu.role.user.publish;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;
import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.SendNotificationProcessImpl;

import javax.xml.ws.Endpoint;

import static edu.bupt.wangfu.module.util.Constant.*;

public class Test implements Runnable {
    public static SendWSNCommandWSSyn register;

    public static SendWSNCommandWSSyn send;

    public static String publishAddress = "";

    private static int first = 0;

    //设置用户id
    public static String id = null;//= String.valueOf(System.currentTimeMillis());

    public Test() {
        register = new SendWSNCommandWSSyn(sendAddr+id, wsnAddr);
        SendNotificationProcessImpl impl = new SendNotificationProcessImpl();
        Endpoint.publish(sendAddr+id, impl);
        register.register(id, sendTopic, sendAddr+id);
    }
    
    public void run(){

        this.sendTest(256);
    }

//    public void sendMethod(String msg) {
//        if (!publishAddress.equals("")) {
//            send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
//            send.publish(id, sendTopic, msg);
//        }else {
//            System.out.println("用户还未获得发布地址，无法发布！");
//        }
//    }

    /**
     * 文本发送测试，单个文本大小为1024B
     * @param num
     */
    public void sendTest(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MyPublisher.pack-13-4; i++) {
            sb.append('a');
        }
        String msg = sb.toString();
        System.out.println(publishAddress);
        send = new SendWSNCommandWSSyn(sendAddr+id, publishAddress);
        //发三倍，防止丢包
        for (int i = first; i < first+num * 1.5; i++) {
//            System.out.println(i);
            send.publish(id, sendTopic, i + ":" + System.currentTimeMillis() + ":" + msg);
        }
        System.out.println("over");
    }

    public static void main (String[] args) throws InterruptedException {
        for(int i = 0;i<10;i++){
            Test test = new Test();
            test.id = String.valueOf(i);
            test.first = i*384;
            Thread thread = new Thread(test);
            Thread.sleep(100);
            thread.start();
        }
    }

}
