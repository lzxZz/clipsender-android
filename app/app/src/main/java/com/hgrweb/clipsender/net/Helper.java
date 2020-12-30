package com.hgrweb.clipsender.net;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Helper {

    public static boolean IPv4Vilid(String ip){
        String ip_body[] = ip.split("\\.");

        if (ip_body.length != 4){
            return  false;
        }
        for (String body : ip_body){
            int val = Integer.valueOf(body);
            if (val > 255|| val < 0){
                return false;
            }
        }
        return true;
    }

   public static ArrayList<String> getIPs(){
        ArrayList<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> enNetworkInterface = NetworkInterface.getNetworkInterfaces(); //获取本机所有的网络接口
            while (enNetworkInterface.hasMoreElements()){
                NetworkInterface networkInterface = enNetworkInterface.nextElement();   //获取 Enumeration 对象中的下一个数据
                if (!networkInterface.isUp()) { // 判断网口是否在使用
                    continue;
                }
                for (InterfaceAddress address : networkInterface.getInterfaceAddresses()){

                    InetAddress broad =  address.getBroadcast();
                    if (broad != null){
                        System.out.println(broad);
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
