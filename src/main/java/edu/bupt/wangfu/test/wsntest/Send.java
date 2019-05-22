package edu.bupt.wangfu.test.wsntest;

import edu.bupt.wangfu.module.wsnMgr.util.soap.SendWSNCommand;

import java.util.Scanner;

public class Send {
    public static String receiveAddr = "http://192.168.100.11:9016/wsn-subscribe";
    private static int pack = 256;
    private static int num = 500;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            pack = scanner.nextInt();
            num = scanner.nextInt();
            System.out.println("pack: " + pack + "\tnum: " + num);
//            SendWSNCommand wsnCommand = new SendWSNCommand();
        }
    }
}
