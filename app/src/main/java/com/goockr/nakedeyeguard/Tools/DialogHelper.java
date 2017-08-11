package com.goockr.nakedeyeguard.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.goockr.nakedeyeguard.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by CMQ on 2017/6/5.
 */

public class DialogHelper {

    ProgressDialog dialog;

    DialogHelperCallBack dialogHelperCallBack;

    TimerTask task;

    Timer timer;

    public  void dialogShow(Activity context,String title)
    {
        dialog = new ProgressDialog(context, R.style.dialog);


        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(title);

        dialog.show();
    }

    
    public  void updateTitleDelayHide(String title,long delay,DialogHelperCallBack callBack)
    {
        if (dialog!=null) dialog.setMessage(title);

        dialogHelperCallBack=callBack;

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }


        task = new TimerTask(){
            public void run(){
                if (dialog!=null)
                {
                    dialog.dismiss();
                    dialog=null;
                    dialogHelperCallBack.hideSuccess();
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, delay);
    }

    public void hide(){

        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }


   public interface  DialogHelperCallBack
    {

        public void hideSuccess();

    }


}
