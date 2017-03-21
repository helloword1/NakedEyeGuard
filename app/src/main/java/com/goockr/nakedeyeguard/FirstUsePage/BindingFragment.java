package com.goockr.nakedeyeguard.FirstUsePage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.Http.HttpHelper;
import com.goockr.nakedeyeguard.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import static cn.bingoogolapple.qrcode.zxing.QRCodeEncoder.syncEncodeQRCode;

/**
 * Created by JJT-ssd on 2017/3/2.
 * 设备绑定
 */

public class BindingFragment extends BaseFragment {
    Bitmap bitmap;
    Button bt_CompleteBinding;
    Handler mHandler;
    ImageView iv_BingdingInfo;
    @Override
    protected int getLoyoutId() {
        return R.layout.binding_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
        eventHandle();
    }

    private void setupUI(View view)
    {
        getBackBtn().setVisibility(View.GONE);
        setWifiIcon(true);
        bt_CompleteBinding=(Button)view.findViewById(R.id.bt_CompleteBinding);
        iv_BingdingInfo=(ImageView)view.findViewById(R.id.iv_BingdingInfo);
    }

    private void eventHandle()
    {
        bt_CompleteBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isBinding=true;
                TextView tv_Reset = new TextView(getActivity());

                tv_Reset.setTextColor(Color.WHITE);
                tv_Reset.setTextSize(18);
                if (isBinding)
                {
                    tv_Reset.setText("您已成功激活设备！");

                }else {
                    tv_Reset.setText("未能成功激活设备，请确认是否已在微信服务号成功注册！");
                }
                final KProgressHUD restHUD= KProgressHUD.create(getActivity())
                        .setCustomView(tv_Reset)
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        restHUD.dismiss();
                        if (isBinding)
                        {
                            replaFragment(new ClauseFragment());
                        }
                    }
                }, 2000);
            }
        });
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what!=1)return false;
                if (msg.arg1==1){
                    iv_BingdingInfo.setImageBitmap(bitmap);
                }
                return false;
            }
        });
        createQRCode();

    }
    public void replaFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_WifiConnect,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 生成二维码
     */
    private void createQRCode()
    {

       new Thread(new Runnable() {
           @Override
           public void run() {
               bitmap = syncEncodeQRCode(HttpHelper.getDeviceId("12345"),220);
               Message msg=new Message();
               msg.what=1;
               msg.arg1=1;
               mHandler.sendMessage(msg);
           }
       }).start();

    }
}
