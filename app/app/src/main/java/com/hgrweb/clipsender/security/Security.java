package com.hgrweb.clipsender.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {

    byte[] iv_bytes = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, -0x80, 
                        0x10, 0x08, 0x20, 0x04, 0x40, 0x02, -0x80, 0x01};
    byte[] zero12 = {   0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    private byte[] MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
                'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            byte[] result = new byte[12];
            System.arraycopy(md, 0, result, 0, 12 );
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] appendZeorTo12Byte(byte[] source){
        byte[] new_byte = new byte[12];
        System.arraycopy(source, 0, new_byte, 0, source.length);
        System.arraycopy(zero12, 0, new_byte, source.length, 12-source.length);
        return new_byte;
    }

    private  byte[] getUnixTimeStamp(){
        long time = System.currentTimeMillis(); //ms
        time = time / 1000 ;  // s
        time = time - (time % 300);
        int n = Integer.valueOf(time+"");

        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    private byte[] generaKey(String src_key) throws UnsupportedEncodingException {
        byte[] key_byte = src_key.getBytes("UTF-8");
        if (key_byte.length <= 12){
            key_byte = appendZeorTo12Byte(key_byte);
        }else{
            // 计算MD5,并截取前12字节
            // TODO 好像没有完成截取前12字节
            key_byte = MD5(src_key);
        }
        key_byte = byteMerger(key_byte, getUnixTimeStamp());
        return key_byte;
    }

    public byte[] encrypt(String sSrc,  String sKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] key_byte = generaKey(sKey);
        SecretKeySpec skeySpec = new SecretKeySpec(key_byte, "AES");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(iv_bytes);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
        return encrypted;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
}
