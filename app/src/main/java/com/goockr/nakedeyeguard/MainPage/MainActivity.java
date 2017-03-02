package com.goockr.nakedeyeguard.MainPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.goockr.nakedeyeguard.Base.ActivityCollector;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.Model.UserModel;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.SettingPage.SettingActivity;
import com.goockr.nakedeyeguard.SettingPage.WifiPage.WifiActivity;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.SimpleOnPullListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    List<UserModel> userModels=new ArrayList<>();
    CoolRefreshView coolRefreshView;
    GridView gv_MainUser;

    WindowManager windowManager;

    ImageButton ib_MainSetting;

    @Override
    protected int getLoyoutId() {return R.layout.activity_main;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initValue();
        setupUI();

    }



    private void setupUI() {

        getBackBtn().setVisibility(View.GONE);
        ib_MainSetting= (ImageButton) findViewById(R.id.ib_MainSetting);
        ib_MainSetting.setOnClickListener(this);
        coolRefreshView= (CoolRefreshView)findViewById(R.id.coolRefreshView);
        coolRefreshView.setPullHeader(new LoadingHeader(),false);
        windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        int spacing=0;
        int marginTop=0;
        gv_MainUser= (GridView)findViewById(R.id.gv_MainUser);
        if (screenHeight>=1080)
        {
            spacing=(screenWidth-720)/4;
            marginTop=66;
            //gv_Scenes.setVerticalSpacing(30);
        }
        else
        {
            spacing=(screenWidth-600)/4;
            marginTop=44;
            //gv_Scenes.setVerticalSpacing(25);
        }
        gv_MainUser.setHorizontalSpacing(spacing);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(gv_MainUser.getLayoutParams());
        lp.setMargins(spacing,marginTop ,spacing, 0);
        gv_MainUser.setLayoutParams(lp);
        GridAdapter gridAdapter=new GridAdapter(this,userModels);
        gv_MainUser.setAdapter(gridAdapter);


        //添加刷新监听
        coolRefreshView.addOnPullListener(new SimpleOnPullListener() {
            @Override
            public void onRefreshing(CoolRefreshView refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        coolRefreshView.setRefreshing(false);
                    }
                },2000);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Activity activty: ActivityCollector.activities) {
                    if (activty.getClass().equals(WifiActivity.class))
                        activty.finish();
                }
            }
        },1000);
    }


    private void initValue()
    {
        for (int i=0;i<10;i++)
        {
            UserModel sceneModel=new UserModel();
            sceneModel.setUserName("图标"+String.valueOf(i));
            sceneModel.setUserIcon(R.drawable.test );
            userModels.add(sceneModel);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ib_MainSetting:
                Intent intent=new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
