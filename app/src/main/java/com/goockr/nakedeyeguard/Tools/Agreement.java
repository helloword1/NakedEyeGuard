package com.goockr.nakedeyeguard.Tools;

/**
 * Created by JJT-ssd on 2016/11/24.
 */

public class Agreement {

    ///////////////////////////////协议 ///////////////////////////////////////////

    public static byte[] TestCode = {(byte) 0x00,(byte) 0x00, (byte) 0x00,(byte) 0x00, (byte) 0x00, (byte) 0x00};
    //操作指令
    // 开始指令0xFE+0x01+0x00+0x00+0x01+0xFF

   // public static byte[] START = {(byte) 0xFE,(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0xFF};

    public static byte[] START(int type,int Intensity)
    {
        int num = type+1+Intensity;
        return new byte[]{(byte) 0xFE,(byte) 0x01, (byte)type, (byte) Intensity, (byte) num, (byte) 0xFF};
    }
//    //暂停指令0xFE+0x04+0x00+0x00+0x04+0xFF
//    public static byte[] SUSPEND = {(byte) 0xFE,(byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xFF};

//    // 继续指令0xFE+0x05+0x00+0x00+0x05+0xFF
//    public static byte[] CONTINUE = {(byte) 0xFE,(byte) 0x05, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0xFF};

    // 查询电流强度指令 0xFE+0x03+0x00+0x00+0x03+0xFF
    public static byte[] CurrentIntensityQuery = {(byte) 0xFE,(byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xFF};

    //结束指令 0xFE+0x06+0x00+0x00+0x06+0xFF
    public static byte[] END = {(byte) 0xFE,(byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0xFF};
    ///////////////////////////////////////////////////////////////////

    // 设置电流强度指令0xFE+0x02+0x01+0x00+0x03+0xFF
    public static byte[] setCurrentIntensity(int numLevel)
    {
        return  createIntensityAgreement((byte)numLevel);
    }

    //协议生成工厂
    private static byte[] createAgreement(byte data)
    {
        int sum=0;
        byte[] bytes ={(byte) 0x02, data, (byte) 0x00};
        //1.计算校验和
        for (int i=0;i<bytes.length;i++)
            sum+=bytes[i];

        //2.生成校验码
        //联合运算
        int dataL = sum&255;
        byte[] result ={(byte) 0xFE,(byte) 0x02, data,(byte) 0x00,(byte)dataL,(byte) 0xFF};
        return result;
    }

    //调节电流协议生成工厂
    private static byte[] createIntensityAgreement(byte data)
    {
        int sum=0;
        byte[] bytes ={(byte) 0x02, data, (byte) data};
        //1.计算校验和
        for (int i=0;i<bytes.length;i++)
            sum+=bytes[i];

        //2.生成校验码
        //联合运算
        int dataL = sum&255;
        byte[] result ={(byte) 0xFE,(byte) 0x02, data,data,(byte)dataL,(byte) 0xFF};
        return result;
    }


    //0xFE+0x05+0x02+0x00+0x07+0xFF
    public static byte[] WorkingHeart = {(byte) 0xFE,(byte) 0x05, (byte) 0x3C,(byte) 0x00, (byte) 0x41, (byte) 0xFF};

}
