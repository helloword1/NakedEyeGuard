package com.goockr.nakedeyeguard.HealingProcessPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.goockr.nakedeyeguard.Base.BaseActivity;
import com.goockr.nakedeyeguard.Http.HttpHelper;
import com.goockr.nakedeyeguard.Model.UserModel;
import com.goockr.nakedeyeguard.R;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class HealingProcessActivity extends BaseActivity {

    public UserModel userModel;
    @Override
    protected int getLoyoutId() {return R.layout.activity_healing_process;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userModel= getIntent().getParcelableExtra("UserModel");
        loadData();
        setupUI();
        replaFragment(new HealingProcessFragment());
    }


    protected void setupUI() {

    }

    private void loadData()
    {

        Map<String,String> map=new HashMap<>();
        map.put("user_id",userModel.getId());
        HttpHelper.httpPost(HttpHelper.getUserInfo(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(HealingProcessActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                //Json的解析类对象
                JSONObject jsonData =null;
                JSONObject results = null;
                JSONObject treatmentNum = null;
                try
                {
                    jsonData=new JSONObject(response);
                    treatmentNum = (JSONObject)jsonData.get("treatment");
                    userModel.setTreatmentTimes(treatmentNum.getString("number"));
                    // results = (JSONObject)jsonData.get("vision");
//                    if (results.length()<=0)return;
//                    for (int i=0;i<results.length();i++)
//                    {
//                        JSONObject dataItem = (JSONObject) results.get(i);
//                        UserModel sceneModel=new UserModel();
//                        sceneModel.setUserName(dataItem.getString("name"));
//                        sceneModel.setUserIconUrl( dataItem.getString("head_image"));
//                        sceneModel.setId(dataItem.getString("id"));
//                    }
                } catch (JSONException e) {}

            }

        });
    }

    public void replaFragment(Fragment fragment)
    {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_HPFLayout,fragment);
        transaction.commit();
    }

}
