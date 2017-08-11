package goockr.stampmachine.JNI;

import android.os.SystemClock;

import android_serialport_api.SerialUtilOld;

/**
 * Created by JJT-ssd on 2016/11/24.
 */

public class SerialPortHelper {
    private  SerialUtilOld serialUtilOld;
    private ReadThread readThread;
    public SerialPortHelper(){}
    static OnReceivedListener mOnReceivedListener;
    public SerialPortHelper(SerialUtilOld serialUtilOld)
    {
        this.serialUtilOld=serialUtilOld;
    }

    public void sendData(byte[] bytes)
    {
        //int test2=bytes[0]&255;
        serialUtilOld.setData(bytes);
    }
//    app.portHelper.startReadThread();//开启线程读串口数据
    public void releaseReadThread()
    {

        mOnReceivedListener=null;
        if (readThread!=null) readThread.interrupt();
    }

    public class ReadThread extends Thread{

        @Override
        public void run() {
            super.run();
            while (!Thread.currentThread().isInterrupted()){
                try {
                    SystemClock.sleep(10);
                    byte[] data= serialUtilOld.getDataByte();
                    if(data!=null)
                    {
                        int[]results = DataProcessing.dataCheck(data,data.length);//校验数据
                        if (results.length>2)
                        {
                            int[]resultsDatas = DataProcessing.dataResult(results);//数据处理
                            SerialPortHelper.mOnReceivedListener.onReceived(resultsDatas);
                        }
                    }
                }catch (NullPointerException e){
                }
                catch (Exception e) {
                }

            }
        }
    }

    public void setOnReceivedListener(OnReceivedListener onReceivedListener) {

        this.mOnReceivedListener=null;
        if (readThread!=null)
        {
            readThread.interrupt();
            readThread=null;
        }
        this.mOnReceivedListener = onReceivedListener;
        readThread=new ReadThread();
        readThread.start();
    }

    public interface OnReceivedListener {
        void onReceived(int[] bytes);
    }

}

