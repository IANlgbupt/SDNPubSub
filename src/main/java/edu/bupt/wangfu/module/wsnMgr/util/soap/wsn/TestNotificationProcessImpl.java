package edu.bupt.wangfu.module.wsnMgr.util.soap.wsn;

import edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess;
import edu.bupt.wangfu.role.user.subscribe.MySubscriber;
import edu.bupt.wangfu.test.wsntest.Receive;

import javax.jws.WebService;

@WebService(endpointInterface= "edu.bupt.wangfu.module.wsnMgr.util.soap.INotificationProcess",
        serviceName="NotificationProcessImpl")
public class TestNotificationProcessImpl implements INotificationProcess {
    @Override
    public void notificationProcess(String notification) {
        String msg = splitString(notification, "<message>", "</message>");
        String[] strings = msg.split(":");
        int id = Integer.parseInt(strings[0]);
        if (id <= Receive.num) {
            Receive.sendTimeList.add(Long.valueOf(strings[1]));
            Receive.receiveTimeList.add(System.currentTimeMillis());
        }else {
            if (Receive.flag) {
                int count = MySubscriber.sendTimeList.size();
                long delay = 0L;
                for (int i = 0; i < count; i++) {
                    delay += Math.abs(MySubscriber.receiveTimeList.get(i) - MySubscriber.sendTimeList.get(i));
                }
                System.out.println("发包总数：" + MySubscriber.num + " 接收总数：" + count + " 总时延：" + delay
                        + " 平均时延：" + delay/count);
                Receive.flag = false;
            }
        }
    }

    public String splitString(String string, String start, String end)
    {
        int from = string.indexOf(start) + start.length();
        int to = string.indexOf(end);
        return string.substring(from, to);
    }
}
