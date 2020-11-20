package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess;
import edu.bupt.wangfu.role.user.subscribe.MySubscriber;
import edu.bupt.wangfu.role.user.util.RTPpacket;
import sun.misc.BASE64Decoder;

import javax.jws.WebService;
import java.awt.*;
import java.io.IOException;

/**
 * 客户端ws处理程序
 * 对应的是wsn层ws处理程序
 */
@WebService(endpointInterface= "edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess",
        serviceName="NotificationProcessImpl")
public class UserNotificationProcessImpl implements INotificationProcess {

    public String  notificationProcess(String notification) {
        String topic = splitString(notification, "<topic>", "</topic>");
        String msg = splitString(notification, "<message>", "</message>");
        if (msg.startsWith("rtp--")) {
            //视频内容
            System.out.println("视频播放中");
            msg = msg.substring(5);
            BASE64Decoder decoder = new BASE64Decoder();
            RTPpacket rtp_packet = null;
            byte[] packet_bits = new byte[0];
            try {
                packet_bits = decoder.decodeBuffer(msg);
            } catch (IOException e) {
                System.out.println("解码错误");
            }
            rtp_packet = new RTPpacket(packet_bits, packet_bits.length);
            int seqNb = rtp_packet.getsequencenumber();
            //get the payload bitstream from the RTPpacket object
            int payload_length = rtp_packet.getpayload_length();
            byte [] payload = new byte[payload_length];
            rtp_packet.getpayload(payload);
            //get an Image object from the payload bitstream
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            MySubscriber.fsynch.addFrame(toolkit.createImage(payload, 0, payload_length), seqNb);
        }else {
            //文本消息
//            MySubscriber.total++;
//            System.out.println(MySubscriber.total);
            System.out.println("收到订阅主题 " + topic + " 文本消息：" + msg);
//            String[] strings = msg.split(":");
//            int id = Integer.parseInt(strings[0]);
//
//            if (id <=600000) {
//                MySubscriber.sendTimeList.add(Long.valueOf(strings[1]));
//                MySubscriber.receiveTimeList.add(System.nanoTime());
////                System.out.println(msg);
//            }else {
//                if (MySubscriber.flag) {
//                    int count = MySubscriber.sendTimeList.size();
//                    long delay = 0L;
//                    for (int i = 0; i < count; i++) {
////                        System.out.println(MySubscriber.receiveTimeList.get(i)+"      "+MySubscriber.sendTimeList.get(i));
//                        delay += Math.abs(MySubscriber.receiveTimeList.get(i) - MySubscriber.sendTimeList.get(i));
//                    }
//                    long delay_2 = Math.abs(MySubscriber.receiveTimeList.get(0) -
//                            MySubscriber.sendTimeList.get(0));
//                    long delay_3 = Math.abs(MySubscriber.receiveTimeList.get(count-1) -
//                            MySubscriber.sendTimeList.get(count-1));
//                    System.out.println(" 接收总数：" + count + " 总时延：" + delay
//                            + " 时延："+delay_2 + "时延：" + delay_3);
//                    MySubscriber.flag = false;
//                }
//            }
////            while(true){
////
////            }
        }
        return topic;
    }

    public String splitString(String string, String start, String end)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }
}