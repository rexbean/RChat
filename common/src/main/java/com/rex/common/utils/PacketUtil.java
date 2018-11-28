package com.rex.common.utils;


import com.rex.common.message.MessageManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;

import static com.rex.common.net.packet.DATA_LEN;
import static com.rex.common.net.packet.HEAD;
import static com.rex.common.net.packet.HEAD_LEN;
import static com.rex.common.net.packet.BUFFER_MAX_LEN;

public class PacketUtil {
    private static final Logger logger = LoggerFactory.getLogger(PacketUtil.class.getSimpleName());
    public static byte[] constructPacket(byte[] data){
        byte[] packet = new byte[data.length + HEAD_LEN + DATA_LEN];
        System.arraycopy(HEAD,0, packet, 0, HEAD_LEN);
        System.arraycopy(TypeUtil.intToByte(data.length), 0, packet, HEAD_LEN, DATA_LEN);
        System.arraycopy(data, 0, packet, HEAD_LEN + DATA_LEN, data.length);
        return packet;
    }
    public static int mergeAndCut(byte[] packet, int dataLen, BufferedInputStream bis){
        if(packet.length == 0) {
            logger.debug("cutStickyPacket");
            return cutStickyPacket(packet, bis);
        } else {
            logger.debug("readAndMerge");
            return readAndMerge(bis, dataLen, packet);
        }
    }

    private static int readAndMerge(BufferedInputStream bis, int dataLen, byte[] packet) {
        try{
            byte[] buffer = new byte[BUFFER_MAX_LEN];
            int len = bis.read(buffer);
            if(len == -1) {
                return -1;
            }
            int needLen = dataLen - packet.length;
            logger.debug("ReadAndMerge needLen = %s", needLen);
            return helper(len, needLen, 0, buffer, packet, bis);
        } catch (IOException e){
            return -1;
        }
    }
    private static int getPacketInfo(byte[] buffer, int start){
        for(int i = 0; i < HEAD_LEN;i++){
            if(buffer[start + i] != HEAD[i]){
                logger.error("validate HEAD failed");
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
                                       final BufferedInputStream bis){
        try{
            byte[] buffer = new byte[BUFFER_MAX_LEN];
            int len = bis.read(buffer);
            if(len < HEAD_LEN + DATA_LEN){
                return -1;
            }
            int dataLen = getPacketInfo(buffer, 0);
            if (dataLen == -1) {
                return -1;
            }
            logger.debug("Cut Sticky packet, dataLen = %s" ,dataLen);
            return helper(len, dataLen + HEAD_LEN + DATA_LEN, 0, buffer, packet, bis);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
    private static int helper(int len,
                              int needLen,
                              int start,
                              byte[] buffer,
                              byte[] packet,
                              BufferedInputStream bis){
        try{
            // whole stream has been used out
            if(len <= 0){
                return needLen;
            }
            int dataLen = needLen;
            // the rest length of the buffer >= needed
            if(len >= needLen){
                if(packet.length == 0) {
                    start = start + HEAD_LEN + DATA_LEN;
                    dataLen = needLen - HEAD_LEN - DATA_LEN;
                }
                packet = saveData(packet, buffer, start, dataLen);

                int rest = len - needLen;
                if(rest < HEAD_LEN + DATA_LEN) {
                    byte[] temp = new byte[HEAD_LEN + DATA_LEN];
                    rest = saveHead(temp, buffer, rest, bis);
                    if(rest == -1){
                        return -1;
                    }
                    start = 0;
                } else {
                    start = rest;
                }
                needLen = getPacketInfo(buffer, start);
                if(needLen == -1){
                    return -1;
                }
                return helper(rest, needLen + HEAD_LEN + DATA_LEN, start, buffer, packet, bis);
            } else {
                start = packet.length == 0 ? start + HEAD_LEN + DATA_LEN : 0;
                packet = merge(packet, buffer, start, len - start);
                needLen -= len;
                len = bis.read(buffer);
                return helper(len, needLen,0, buffer, packet, bis);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static byte[] saveData(byte[] packet, byte[] buffer, int start, int  dataLen){
        packet = merge(packet, buffer, start, dataLen);
        MessageManager.getInstance().add(packet);
        logger.debug("Message has been added");
        packet = new byte[0];
        return packet;
    }

    private static int saveHead(byte[] temp,
                                byte[] buffer,
                                int rest,
                                BufferedInputStream bis){
        try{
            System.arraycopy(buffer, rest, temp,0, rest);
            int len = bis.read(buffer);
            if(len == -1){
                return -1;
            }
            System.arraycopy(buffer, 0, temp, rest, HEAD_LEN + DATA_LEN - rest);
            return len;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

    }
    private static byte[] merge(byte[] packet, final byte[] buffer, int start, int len){
        logger.debug(start + " " + packet.length + " " + len + " " + buffer.length);
        byte[] temp = new byte[packet.length + len];
        System.arraycopy(packet,0, temp, 0, packet.length);
        System.arraycopy(buffer, start, temp, packet.length, len);
        return temp;
    }
}
