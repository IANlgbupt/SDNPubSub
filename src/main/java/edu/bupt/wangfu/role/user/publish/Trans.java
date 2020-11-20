package edu.bupt.wangfu.role.user.publish;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommandWSSyn;

public class Trans {
	public static SendWSNCommandWSSyn register;

	public static SendWSNCommandWSSyn send;

	public String topic;
	public String sendAddr;
	public String wsnAddr;

	private String id;
	public String publishAddress = "";

	private static int i = 0;

	//设置用户id


	public void register()
	{
		register = new SendWSNCommandWSSyn(sendAddr, wsnAddr);

		publishAddress = register.register(this.id, topic, sendAddr);
		System.out.println(publishAddress);
	}

	public Trans(String wsnAddr,String sendAddr,String sendTopic)
	{
		this.wsnAddr = wsnAddr;
		this.sendAddr = sendAddr;
		this.topic = sendTopic;
		register = new SendWSNCommandWSSyn(sendAddr, wsnAddr);
		this.id = String.valueOf(System.currentTimeMillis());
		publishAddress = register.register(id, topic, this.sendAddr);
	}
	public Trans() {
		register = new SendWSNCommandWSSyn(sendAddr, wsnAddr);
//		SendNotificationProcessImpl impl = new SendNotificationProcessImpl();
//		Endpoint.publish(sendAddr, impl);
		//publishAddress = register.register(id, sendTopic, sendAddr);
		//System.out.println(publishAddress);
	}

	public void sendMethod(String msg) {
		if (!publishAddress.equals("")) {
			send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
			send.publish(id, topic, msg);
		}else {
			System.out.println("用户还未获得发布地址，无法发布！");
		}
	}

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
		send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
		//发三倍，防止丢包
		for (int i = 0; i < MyPublisher.num * 1.5; i++) {
			//send.publish(id, sendTopic, i + ":" + System.currentTimeMillis() + ":" + msg);
		}
		System.out.println("over");
	}

	public void sendTestWithSleep(int num) {
		if (!publishAddress.equals("")) {
			send = new SendWSNCommandWSSyn(sendAddr, publishAddress);
		}else {
			System.out.println("用户还未获得发布地址，无法发布！");
		}
		//send.publish(id, sendTopic, String.valueOf(System.currentTimeMillis()));
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < 1024-13-4; i++) {
//			sb.append('a');
//		}
//		String msg = sb.toString();
//		//发三倍，防止丢包
//		long startTime = System.currentTimeMillis(), endTime;
//		for (int i = 0; i < num * 1.5; i++) {
//			send.publish(id, sendTopic, i + ":" + System.currentTimeMillis() + ":" + msg);
//			endTime = System.currentTimeMillis();
//			if (endTime - startTime > 1000) {
//				try {
//					Thread.sleep(endTime - startTime);
//					startTime = System.currentTimeMillis();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		System.out.println("over");
	}

}
