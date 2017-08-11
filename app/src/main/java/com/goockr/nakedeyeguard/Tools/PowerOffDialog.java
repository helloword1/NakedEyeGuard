package com.goockr.nakedeyeguard.Tools;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.R;

import java.io.IOException;

/**
 * Created by CMQ on 2017/6/6.
 */

public class PowerOffDialog {



    public static void show(Context context, PowerOffDialogCallBack cb)
    {
        final PowerOffDialogCallBack callBack=cb;

        final MaterialDialog resetDialog =  new MaterialDialog.Builder(context)
                .customView(R.layout.poweroff_dialog,true)
                .show();
        resetDialog.setCanceledOnTouchOutside(false);
        View resetView = resetDialog.getCustomView();
        Button bt_SetResetCancle = (Button) resetView.findViewById(R.id.bt_poweroffcancle);
        Button bt_SetResetSure = (Button) resetView.findViewById(R.id.bt_poweroffsure);
        bt_SetResetCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetDialog.dismiss();
                callBack.powerOffCancle();

            }
        });
        bt_SetResetSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDialog.dismiss();
                Process Proc = null;
                try {
                    Proc = Runtime.getRuntime().exec("./system/bin/shutdown.sh");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void show(Context context, String tip,PowerOffDialogCallBack cb)
    {
        final PowerOffDialogCallBack callBack=cb;

        final MaterialDialog resetDialog =  new MaterialDialog.Builder(context)
                .customView(R.layout.poweroff_dialog,true)
                .show();
        resetDialog.setCanceledOnTouchOutside(false);
        View resetView = resetDialog.getCustomView();
        Button bt_SetResetCancle = (Button) resetView.findViewById(R.id.bt_poweroffcancle);
        Button bt_SetResetSure = (Button) resetView.findViewById(R.id.bt_poweroffsure);
        TextView tip_tv=(TextView) resetView.findViewById(R.id.tv_powertip);
        tip_tv.setText(tip);

        bt_SetResetCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetDialog.dismiss();
                callBack.powerOffCancle();

            }
        });
        bt_SetResetSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDialog.dismiss();
                Process Proc = null;
                try {
                    Proc = Runtime.getRuntime().exec("./system/bin/shutdown.sh");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public interface PowerOffDialogCallBack
    {
        public void powerOffCancle();
    }

}

