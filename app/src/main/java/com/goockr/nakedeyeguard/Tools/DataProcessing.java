package goockr.stampmachine.JNI;

/**
 * Created by JJT-ssd on 2016/12/1.
 */

public class DataProcessing {

    private static int[] resultData=new int[5];
    //转译方法
    public static byte[] transfer(char[] buff_in, int data_len)
    {
        byte[]buff_out=new byte[20];
        int Count = 0, index;
        buff_out[Count++] = 0x7e;
        for(index = 1; index < data_len - 1; index++)
        {
            switch(buff_in[index])
            {
                case 0x7d:
                    buff_out[Count++] = 0x7d;
                    buff_out[Count++] = 0x00;
                    break;

                case 0x7e:
                    buff_out[Count++] = 0x7d;
                    buff_out[Count++] = 0x01;
                    break;

                case 0x7f:
                    buff_out[Count++]= 0x7d;
                    buff_out[Count++] = 0x02;
                    break;

                default:
                    buff_out[Count++] = (byte)buff_in[index];
            }
        }
        buff_out[Count++] = 0x7f;
        byte[]buff_result=new byte[Count];
        for (int i=0;i<Count;i++)
        {
            buff_result[i]=buff_out[i];
        }
        return buff_result;
    }

    /////数据反转译与校验处理///
    public static int[] dataCheck(byte dataBuff[] , int data_len)
    {
        int buff_out[]=new int[32];
        int count=0;//转译后的数据长度
        int sum=0;//校验和
        //1.数据结构判断
        //数据格式错误，直接返回
        if((dataBuff[0]&255)!=126&&(dataBuff[data_len-1]&255)!=127) return buff_out;
        //校验码错误，直接返回
       // if((dataBuff[2]&255)!=0) return buff_out;
        //2.指令合法，做数据处理
        //2.1 数据转码
        for (int i = 0; i <data_len; ++i) {
            if((dataBuff[i]&255)==125)
            {
                switch (dataBuff[++i])
                {
                    case 0x00:
                        buff_out[count++]=125;//0x7d
                        break;
                    case 0x01:
                        buff_out[count++]=126;//0x7e
                        break;
                    case 0x02:
                        buff_out[count++]=127;//0x7f
                        break;
                }
            } else
            {
                buff_out[count++]=(dataBuff[i]&255);
            }
        }
        //2.2数据校验
        //计算校验和
        for (int i = 1; i <count-3 ; ++i) {
            sum= sum+buff_out[i];
        }
        //联合运算
        int dataH = buff_out[count-2];//)&65280;
        int dataL =  + buff_out[count-3]&255;
        int checkNum =((dataH << 8)&65280)+dataL;
        //和校验
        if((0xffff^checkNum)+1 !=sum) return new int[]{0};
        int[] resultDate=new int[count];
        System.arraycopy(buff_out, 0, resultDate, 0, count);
        return resultDate;
    }

    public static int[]dataResult(int[] data)
    {

        switch (data[1])
        {
            case 129://0x81移动指令回复
                resultData[0]=129;
                resultData[1]=data[2];
                break;
            case 130://0x82复位指令回复
                resultData[0]=130;
                resultData[1]=data[2];
                break;
            case 131://0x83查询坐标回复
                resultData[0]=131;
                resultData[1]=data[2];
                resultData[2]=data[3];//X坐标值
                resultData[3]=data[4];//Y坐标值
                resultData[4]=data[5];
                break;
            case 132://0x84仓门控制回复
                resultData[0]=132;
                resultData[1]=data[2];
                break;
            case 133://0x85盖章指令回复
                resultData[0]=133;
                resultData[1]=data[2];
                break;
            case 134://0x86盖章完成回复
                resultData[0]=134;
                resultData[1]=data[2];
                break;
            case 135://0x87指纹创建回复
                resultData[0]=135;
                resultData[1]=data[2];
                break;
            case 136://0x88创建指纹模板结果回复
                resultData[0]=136;
                resultData[1]=data[2];
                resultData[2]=data[3];
                break;
            case 137://0x89修改指纹模板回复
                resultData[0]=137;
                resultData[1]=data[2];
                break;
            case 138://0x8a修改指纹模板结果回复
                resultData[0]=138;
                resultData[1]=data[2];
                resultData[2]=data[3];
                break;
            case 139://0x8b删除指纹模板
                resultData[0]=139;
                resultData[1]=data[2];
                break;
            case 140://0x8c指纹匹配
                resultData[0]=140;
                resultData[1]=data[2];
                break;
            case 141://0x8d指纹匹配结果返回
                resultData[0]=141;
                resultData[1]=data[2];
                resultData[2]=data[3];
                break;
            case 142://0x8e蜂鸣器控制指令
                resultData[0]=142;
                resultData[1]=data[2];
                break;
            case 143://0x8f选章控制指令
                resultData[0]=143;
                resultData[1]=data[2];
                break;
            case 144://开门完成指令
                resultData[0]=144;
                resultData[1]=data[2];
                break;
            case 145://关门完成指令
                resultData[0]=145;
                resultData[1]=data[2];
                break;
            case 146://复位完成指令
                resultData[0]=146;
                resultData[1]=data[2];
                break;
            case 147://清空指纹库
                resultData[0]=147;
                resultData[1]=data[2];
                break;
            case 150://清空指纹库
                resultData[0]=150;
                resultData[1]=data[2];
                resultData[2]=data[3];
                break;
            default:
                break;
        }
        return resultData;
    }

}


