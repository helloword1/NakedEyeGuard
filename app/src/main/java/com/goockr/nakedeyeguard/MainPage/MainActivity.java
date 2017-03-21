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
import android.widget.Toast;

import com.goockr.nakedeyeguard.Base.ActivityCollector;
import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.Http.HttpHelper;
import com.goockr.nakedeyeguard.Model.UserModel;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.SettingPage.SettingActivity;
import com.goockr.nakedeyeguard.SettingPage.WifiPage.WifiActivity;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.SimpleOnPullListener;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    List<UserModel> userModels=new ArrayList<>();
    CoolRefreshView coolRefreshView;
    GridView gv_MainUser;
    WindowManager windowManager;
    ImageButton ib_MainSetting;
    GridAdapter gridAdapter;
   // ScreenReceiver screenReceiver;

    @Override
    protected int getLoyoutId() {return R.layout.activity_main;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
       //刷新数据
        coolRefreshView.setRefreshing(true);
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
        gridAdapter=new GridAdapter(this,userModels);
        gv_MainUser.setAdapter(gridAdapter);

        //添加刷新监听
        coolRefreshView.addOnPullListener(new SimpleOnPullListener() {
            @Override
            public void onRefreshing(CoolRefreshView refreshView) {

                loadData();
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

//    private void initValue()
//    {
//        screenReceiver=new ScreenReceiver();
//        screenReceiver.registerReceiver(this);
//    }

    private void loadData()
    {
        if (userModels.size()>0) userModels.clear();
        Map<String,String> map=new HashMap<>();
        map.put("c_pad_id",HttpHelper.deviceId);
        HttpHelper.httpPost(HttpHelper.getUserList(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MainActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                //Json的解析类对象
                JSONArray results = null;
                try
                {
                    results = new JSONArray(response);
                    if (results.length()<=0)return;
                    for (int i=0;i<results.length();i++)
                    {
                        JSONObject dataItem = (JSONObject) results.get(i);
                        UserModel sceneModel=new UserModel();
                        sceneModel.setUserName(dataItem.getString("name"));
                        sceneModel.setUserIconUrl( dataItem.getString("head_image"));
                        sceneModel.setId(dataItem.getString("id"));
                        userModels.add(sceneModel);
                    }
                    gridAdapter.notifyDataSetChanged();
                } catch (JSONException e) {}
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // screenReceiver.unregisterReceiver();
    }
}
