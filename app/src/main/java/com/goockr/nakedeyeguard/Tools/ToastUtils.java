package com.goockr.nakedeyeguard.Tools;

import android.content.Context;
import android.widget.Toast;

/**
 * @author rookie
 * @Description: (提示消息)
 * @date 2016/3/15 15:00
 */
public class ToastUtils {
    protected static Toast toast   = null;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(Context context, String s){
        if(StringUtils.isEmpty(s)){ //不显示空消息
            return;
        }
        if(toast==null){
            toast =Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime=System.currentTimeMillis();
        }else{
            twoTime=System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime>Toast.LENGTH_SHORT){
                    toast.show();
                }
            }else{
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }

    public static void showToast(Context context, int resId){
        showToast(context, context.getString(resId));
    }

    public static void showToast(int resId){
        showToast(App.getContext(), App.getAppResources().getString(resId));
    }
    public static void showToast(String s){
        showToast(App.getContext(), s);
    }
}
