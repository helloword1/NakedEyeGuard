package com.goockr.nakedeyeguard.view.HealingProcessPage;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.net.Http.HttpHelper;
import com.goockr.nakedeyeguard.data.Model.UserBean;
import com.goockr.nakedeyeguard.data.Model.UserDetailBean;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.view.TipsPage.GuideFragment;
import com.goockr.nakedeyeguard.Tools.DialogHelper;
import com.goockr.nakedeyeguard.Tools.WifiHelper;
import com.goockr.nakedeyeguard.net.callBack.UserCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by JJT-ssd on 2017/2/28.
 */

public class HealingProcessFragment extends BaseFragment implements View.OnClickListener {
    Button bt_HPFSure;
    UserDetailBean userInfo;
    UserBean userBean;
    CircleImageView iv_HPFUserIcon1;
    TextView tv_HPFUserName;

    TextView tv_BeforeRight;
    TextView tv_BeforeLeft;
    TextView tv_BeforeGlsRight;
    TextView tv_BeforeGlsLeft;
    TextView tv_NowNakedRight;
    TextView tv_NowNakedLeft;
    TextView tv_NowGlsRight;
    TextView tv_NowGlsLeft;

    DialogHelper dialogHelper;
    HealingProcessActivity activity;
    public DialogHelper getDialogHelper() {

        if (dialogHelper == null) {
            dialogHelper = new DialogHelper();
        }
        return dialogHelper;
    }

    @Override
    protected int getLoyoutId() {
        return R.layout.heading_process_fragment;
    }

    @Override
    protected void onCusCreate(View view) {
        userBean = ((HealingProcessActivity) getActivity()).userBean;
        setupUI(view);
        tv_HPFUserName.setText(userBean.getName());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 800);
    }

    @Override
    public void onResume() {
        bt_HPFSure.setEnabled(true);
        super.onResume();
    }

    @Override
    public void onPause() {

        bt_HPFSure.setEnabled(true);

        super.onPause();
    }

    private void setupUI(View view) {

        tv_HPFUserName = (TextView) view.findViewById(R.id.tv_HPFUserName);
        iv_HPFUserIcon1 = (CircleImageView) view.findViewById(R.id.iv_HPFUserIcon);
        bt_HPFSure = (Button) view.findViewById(R.id.bt_HPFSure);
        bt_HPFSure.setClickable(false);

        tv_BeforeRight = (TextView) view.findViewById(R.id.tv_BeforeRight);
        tv_BeforeLeft = (TextView) view.findViewById(R.id.tv_BeforeLeft);
        tv_BeforeGlsRight = (TextView) view.findViewById(R.id.tv_BeforeGlsRight);
        tv_BeforeGlsLeft = (TextView) view.findViewById(R.id.tv_BeforeGlsLeft);
        tv_NowNakedRight = (TextView) view.findViewById(R.id.tv_NowNakedRight);
        tv_NowNakedLeft = (TextView) view.findViewById(R.id.tv_NowNakedLeft);
        tv_NowGlsRight = (TextView) view.findViewById(R.id.tv_NowGlsRight);
        tv_NowGlsLeft = (TextView) view.findViewById(R.id.tv_NowGlsLeft);

        bt_HPFSure.setOnClickListener(this);
         activity = (HealingProcessActivity) getActivity();
        activity.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();

            }
        });
        ImageLoader.getInstance().displayImage(userBean.getHead_image(), iv_HPFUserIcon1);
    }

    /**
     * 加载数据
     */
    private void loadData() {

        if (!WifiHelper.isNetworkAvailable(activity)) {
            final MaterialDialog voiceDialog = new MaterialDialog.Builder(getActivity())
                    .customView(R.layout.nonet_tip, true)
                    .show();
            voiceDialog.setCanceledOnTouchOutside(false);
            View voiceView = voiceDialog.getCustomView();
            Button bt_NoMoneyKonw = (Button) voiceView.findViewById(R.id.bt_nonet);
            bt_NoMoneyKonw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    voiceDialog.dismiss();
                    getActivity().finish();
                }
            });
            return;
        }

        getDialogHelper().dialogShow(getActivity(), "正在获取视力信息...");


        Map<String, String> map = new HashMap<>();
        map.put("c_pad_id", HttpHelper.deviceId);
        map.put("user_id", userBean.getId());
        HttpHelper.httpPost(HttpHelper.getUserInfo(), map, new UserCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                loadDataFiale();
            }

            @Override
            public void onResponse(UserDetailBean response, int id) {


                userInfo = response;
                remainingNumber = response.getLeft_times();
                if (getActivity() != null) {
                    ((HealingProcessActivity) getActivity()).userInfo = userInfo;

                    setData(userInfo);
                    dialogHelper.updateTitleDelayHide("获取视力信息成功", 500, new DialogHelper.DialogHelperCallBack() {
                        @Override
                        public void hideSuccess() {
                            dialogHelper = null;
                        }
                    });

                }

            }
        });
    }

    private void loadDataFiale() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialogHelper != null) {
                        dialogHelper.updateTitleDelayHide("获取视力信息失败", 500, new DialogHelper.DialogHelperCallBack() {
                            @Override
                            public void hideSuccess() {
                                dialogHelper = null;
                            }
                        });
                    }

                    final MaterialDialog resetDialog = new MaterialDialog.Builder(getActivity())
                            .customView(R.layout.load_info_dialog, true)
                            .show();
                    resetDialog.setCanceledOnTouchOutside(false);
                    View resetView = resetDialog.getCustomView();
                    Button bt_SetResetCancle = (Button) resetView.findViewById(R.id.bt_loadinfocancle);
                    Button bt_SetResetSure = (Button) resetView.findViewById(R.id.bt_loadinfosure);
                    bt_SetResetCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            resetDialog.dismiss();
                            getActivity().finish();
                        }
                    });
                    bt_SetResetSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resetDialog.dismiss();
                            loadData();
                        }
                    });


                }
            });
        }

    }


    /**
     * 数据显示
     */
    private void setData(UserDetailBean userInfo) {
        UserDetailBean.VisionBean visionBean = userInfo.getVision();
        tv_BeforeRight.setText(visionBean.getBefore_right());
        tv_BeforeLeft.setText(visionBean.getBefore_left());
        tv_BeforeGlsRight.setText(visionBean.getBefore_gls_right());
        tv_BeforeGlsLeft.setText(visionBean.getBefore_gls_left());
        tv_NowNakedRight.setText(visionBean.getNaked_right());
        tv_NowNakedLeft.setText(visionBean.getNaked_left());
        tv_NowGlsRight.setText(visionBean.getGlasses_right());
        tv_NowGlsLeft.setText(visionBean.getGlasses_left());
        bt_HPFSure.setClickable(true);
    }

    private static int remainingNumber = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_HPFSure:
                if (userInfo == null || TextUtils.isEmpty(userInfo.getName())) {

                    return;
                }

                if (remainingNumber <= 0) {
                    nomenyTips();
                    return;
                }

                bt_HPFSure.setEnabled(false);
                if (TextUtils.isEmpty(userInfo.getTreatment().getType())) return;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (bt_HPFSure != null) {
                            bt_HPFSure.setEnabled(true);
                        }
                    }
                }, 2000);

                replaFragment(new GuideFragment());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialogHelper !=null ){
            dialogHelper.hide();
        }
    }

    /**
     * 没有钱了，弹框提示
     */
    private void nomenyTips() {
        final MaterialDialog voiceDialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.no_money_dialog, true)
                .show();
        voiceDialog.setCanceledOnTouchOutside(false);
        View voiceView = voiceDialog.getCustomView();
        Button bt_NoMoneyKonw = (Button) voiceView.findViewById(R.id.bt_NoMoneyKonw);
        bt_NoMoneyKonw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceDialog.dismiss();
            }
        });
    }

    /**
     * 返回按键检测
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            getActivity().finish();
        }
        return true;
    }
    public void replaFragment(Fragment fragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_HPFLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
