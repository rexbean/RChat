package com.rex.common.utils;

import com.rex.common.message.MessageManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.rex.common.net.packet.DATA_LEN;
import static com.rex.common.net.packet.HEAD;
import static com.rex.common.net.packet.HEAD_LEN;
import static com.rex.common.net.packet.PACKET_MAX_LEN;

public class PacketUtil {


    public static byte[] constructPacket(byte[] data){
        byte[] packet = new byte[data.length + HEAD_LEN + DATA_LEN];
        packet = merge(packet,HEAD,0,HEAD_LEN);
        packet = merge(packet, TypeUtil.intToByte(data.length), 0, DATA_LEN);
        packet = merge(packet, data, 0, data.length);
        return packet;
    }
    public static int mergeAndCut(byte[] packet, int dataLen, BufferedInputStream bis){
        byte[] buffer = new byte[PACKET_MAX_LEN];
        try{
            int len = bis.read(buffer);
            if(len == -1) {
                return -1;
            }
            if(packet.length == 0){
                dataLen = getPacketInfo(buffer, 0);
                dataLen = PacketUtil.cutStickyPacket(packet, bis, dataLen);

            } else {
                if(dataLen - packet.length > bis.available()){
                    return -1;
                } else {
                    dataLen = PacketUtil.cutStickyPacket(packet, bis, dataLen);
                }
            }
            return dataLen;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private static int getPacketInfo(byte[] buffer, int start){
        for(int i = 0; i < HEAD_LEN;i++){
            if(buffer[start + i] != HEAD[i]){
                return -1;
            }
        }
        byte[] dataLen = new byte[DATA_LEN];
        for(int i = 0; i < DATA_LEN; i++){
            dataLen[i] = buffer[start + HEAD_LEN + i];
        }
        return TypeUtil.getInt(dataLen);
    }


    private static int cutStickyPacket(byte[] packet,
                                       final BufferedInputStream bis,
                                       int dataLen){
        try {
            int len = 0;
            int start = 0;
            byte[] buffer = new byte[PACKET_MAX_LEN];
            while ((len = bis.read(buffer)) != -1) {
                while (start >= len) {
                    if (len < dataLen - packet.length) {
                        packet = merge(packet, buffer, 0, dataLen - packet.length);
                        start = buffer.length;
                    } else {
                        packet = merge(packet, buffer, start, dataLen - packet.length);
                        start += dataLen - packet.length;
                        MessageManager.getInstance().add(packet);
                        packet = new byte[0];
                        dataLen = getPacketInfo(buffer, start);
                        if (dataLen == -1) {
                            break;
                        }
                    }
                }
            }
            return dataLen;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static byte[] merge(byte[] packet, final byte[] buffer, int start, int len){
        byte[] temp = new byte[packet.length + len];
        System.arraycopy(packet,0, temp, 0, packet.length);
        System.arraycopy(buffer, start, temp, packet.length, len);
        return temp;
    }
}
