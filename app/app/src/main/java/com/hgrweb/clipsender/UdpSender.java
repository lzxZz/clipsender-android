package com.hgrweb.clipsender;


import java.io.IOException;
import java.net.*;

public class UdpSender {
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    public void sendMsg(byte[] data, String netAddress, int port){
        try {
            datagramSocket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(netAddress);
            datagramPacket = new DatagramPacket(data, data.length, address, port);

            datagramSocket.send(datagramPacket);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}
