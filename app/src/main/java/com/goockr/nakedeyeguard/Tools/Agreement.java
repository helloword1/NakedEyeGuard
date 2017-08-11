package goockr.stampmachine.JNI;

/**
 * Created by JJT-ssd on 2016/11/24.
 */

public class Agreement {
    ///////////////////////////////协议 ///////////////////////////////////////////

    public static byte[] TestCode = {(byte) 0x55,(byte) 0x55, (byte) 0x55};
    //1.电机操作指令
    // 电机复位指令0x7E+0x02+0xFE+0xFF+0x7F
    public static byte[] motorReset = {(byte) 0x7E,(byte) 0x02, (byte) 0xfe, (byte) 0xff, (byte) 0x7f};
    public static byte[] motorResetComplete = {(byte) 0x7E,(byte) 0x12, (byte) 0xee, (byte) 0xff, (byte) 0x7f};
    // 电机查询坐标指令0x7E+0x02+0xFE+0xFF+0x7F
    public static byte[] motorQuery = {(byte) 0x7E,(byte) 0x03, (byte) 0xfd, (byte) 0xff, (byte) 0x7f};

    // 开仓门
    public static byte[] OpenDoor = {(byte) 0x7e,(byte) 0x04, (byte) 0x01, (byte) 0xfb, (byte) 0xff, (byte) 0x7f};
    //开仓门完成
    public static byte[] OpenDoorComplete= {(byte) 0x7e,(byte) 0x10, (byte) 0xf0, (byte) 0xff, (byte) 0x7f};

    //关仓门
    public static byte[] CloseDoor = {(byte) 0x7e,(byte) 0x04, (byte) 0x00, (byte) 0xfc, (byte) 0xff, (byte) 0x7f};
    //关仓门完成
    public static byte[] CloseDoorComplete= {(byte) 0x7e,(byte) 0x11, (byte) 0xef, (byte) 0xff, (byte) 0x7f};

    //盖章指令
    public static byte[] SealCode = {(byte) 0x7e,(byte) 0x05, (byte) 0xfb, (byte) 0xff, (byte) 0x7f};
    //盖章完成
    public static byte[] SealComplete= {(byte) 0x7e,(byte) 0x06, (byte) 0xfa, (byte) 0xff, (byte) 0x7f};

    //2.指纹识别指令
    //匹配指纹
    public static byte[] FingerprintMatching = {(byte) 0x7E,(byte) 0x0c, (byte) 0xf4, (byte) 0xff, (byte) 0x7f};
    //匹配指纹结果回复
    public static byte[] FingerprintFeedBack = {(byte) 0x7E,(byte) 0x0d, (byte) 0xf3, (byte) 0xff, (byte) 0x7f};

    //创建指纹模板
    public static byte[] CreateFingerprint = {(byte) 0x7E,(char) 0x07, (byte) 0xf9, (byte) 0xff, (byte) 0x7f};
    //创建指纹结果回复
    public static byte[] CreateFingerprintFeedBack = {(byte) 0x7E,(char) 0x08, (byte) 0xf8, (byte) 0xff, (byte) 0x7f};

    //修改指纹结果回复
    public static byte[] ModifyFingerprintFeedBack = {(byte) 0x7E,(char) 0x0a, (byte) 0xf6,(byte) 0xff,(byte) 0x7f};
    //清空指纹库
    public static byte[] ResetFingerprint = {(byte) 0x7E,(char) 0x13, (byte) 0xed,(byte) 0xff,(byte) 0x7f};

    /**3.蜂鸣器控制指令*/
    public static byte[] Buzzer = {(byte) 0x7E,(byte) 0x0e, (byte) 0xf2, (byte) 0xff, (byte) 0x7f};

    /**印油量查询指令*/
    public static byte[] LackOfink = {(byte) 0x7E,(byte) 0x16, (byte) 0xea, (byte) 0xff, (byte) 0x7f};

    /**开安全门指令*/
    public static byte[] OpenSecurityDoor = {(byte) 0x7E,(byte) 0x14, (byte) 0xec, (byte) 0xff, (byte) 0x7f};

    //添加印油，印油电磁阀控制指令
    public static byte[] AddOilOpen = {(byte) 0x7E,(byte) 0x15, (byte) 0x01,(byte) 0xea, (byte) 0xff, (byte) 0x7f};
    public static byte[] AddOilClose = {(byte) 0x7E,(byte) 0x15, (byte) 0x00,(byte) 0xeb, (byte) 0xff, (byte) 0x7f};

    ///////////////////////////////////////////////////////////////////

    //修改指纹模板
    public static byte[] modifyFingerprint(String fingerNum)
    {
        Byte tempNum = Byte.valueOf(fingerNum);
        byte [] bytes ={0x09,tempNum.byteValue()};
        return  createAgreement(bytes);
    }

    //删除指纹模板
    public static byte[] deleteFingerprint(String fingerNum)
    {
        Byte tempNum = Byte.valueOf(fingerNum);
        byte [] bytes ={0x0b,tempNum.byteValue()};
        return  createAgreement(bytes);
    }

    //选章指令模板 1,2,3,4
    public static byte[] selectSeal(int fingerNum)
    {
        if (fingerNum==0)//不允许为0，默认是1
            fingerNum=1;
        byte [] bytes ={0x0f,(byte)fingerNum};
        return  createAgreement(bytes);
    }

    //协议生成工厂
    private static byte[] createAgreement( byte[] bytes)
    {
        int sum=0;
        int len=bytes.length;
        //1.计算校验和
        for (int i=0;i<len;i++)
            sum+=bytes[i];
        sum= ~sum+1;
        //2.生成校验码
        //联合运算
        int dataH = (sum&65280)>>8;//)&65280;
        int dataL = sum&255;
        byte[] result =new byte[len+4];
        result[0]=0x7e;
        for (int j=0;j<len;j++)
            result[j+1]=bytes[j];
        result[len+1]=(byte) dataL;
        result[len+2]=(byte) dataH;
        result[len+3]=0x7f;
        return result;
    }

}
