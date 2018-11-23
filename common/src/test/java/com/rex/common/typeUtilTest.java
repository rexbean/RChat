package com.rex.common;

import com.rex.common.utils.TypeUtil;

import org.junit.Assert;
import org.junit.Test;

public class typeUtilTest {
    @Test
    public void getIntTest(){
        byte[] num = new byte[]{(byte)0x25, (byte)0x00};
        int res = TypeUtil.getInt(num);
        Assert.assertEquals(37,res);
    }

    @Test
    public void intToByteTest(){
        int i = 37;
        byte[] res = TypeUtil.intToByte(i);
        Assert.assertEquals(i,TypeUtil.getInt(res));
    }
}
