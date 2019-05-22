package edu.bupt.wangfu.test.wsntest;

import edu.bupt.wangfu.module.wsnMgr.util.soap.wsn.TestNotificationProcessImpl;

import javax.xml.ws.Endpoint;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Receive {
    public static String wsnAddr = "http://192.168.100.11:9010/wsn-core";
    public static int num = 500;
    public static List<Long> sendTimeList;
    public static List<Long> receiveTimeList;
    public static boolean flag;

    public static void main(String[] args) {
        // 消息处理逻辑
        TestNotificationProcessImpl implementor = new TestNotificationProcessImpl();
        // 开启接收服务
        Endpoint endpint = Endpoint.publish(wsnAddr, implementor);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            num = scanner.nextInt();
            flag = true;
            sendTimeList = new LinkedList<>();
            receiveTimeList = new LinkedList<>();
            System.out.println("num: " + num);
        }
    }
}
