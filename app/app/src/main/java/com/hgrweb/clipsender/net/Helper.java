package com.hgrweb.clipsender.net;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Helper {

    public static boolean IPv4Vilid(String ip) {
        String ip_body[] = ip.split("\\.");
        if (ip_body.length != 4) {
            return false;
        }
        for (String body : ip_body) {
            int val = Integer.valueOf(body);
            if (val > 255 || val < 0) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> getIPs() {
        ArrayList<String> ips = new ArrayList<>();
        try {
            //获取本机所有的网络接口
            Enumeration<NetworkInterface> enNetworkInterface =
                NetworkInterface.getNetworkInterfaces(); 
            while (enNetworkInterface.hasMoreElements()) {
                //获取 Enumeration 对象中的下一个数据
                NetworkInterface networkInterface = 
                    enNetworkInterface.nextElement();   
                // 判断网口是否在使用
                if (!networkInterface.isUp()) { 
                    continue;
                }
                for (InterfaceAddress address : 
                        networkInterface.getInterfaceAddresses()) {
                    InetAddress broad = address.getBroadcast();
                    if (broad != null) {
                        ips.add(broad.toString().substring(1));
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ips;
    }
}
