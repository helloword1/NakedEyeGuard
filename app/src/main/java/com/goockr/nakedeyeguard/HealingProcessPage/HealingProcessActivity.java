package com.goockr.nakedeyeguard.HealingProcessPage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
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
    private Handler infoHandler;
    @Override
    protected int getLoyoutId() {return R.layout.activity_healing_process;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userModel= getIntent().getParcelableExtra("UserModel");
        loadData();
        setupUI();

        replaFragment(this,new HealingProcessFragment(),R.id.fl_HPFLayout,"HealingProcessFragment");

    }
    public  void replaFragment(FragmentActivity activity, Fragment fragment, int Rid,String tag)
    {
        FragmentTransaction transaction=activity.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(tag);
        transaction.replace(Rid,fragment);
        transaction.commit();
    }

    protected void setupUI() {

    }

    public void setHandler(Handler handler) {
        infoHandler = handler;
    }

    /**
     * 加载数据
     */
    private void loadData()
    {
        infoHandler=new Handler();
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
                JSONObject jsonVision = null;
                JSONObject treatmentNum = null;
                try
                {
                    jsonData=new JSONObject(response);
                    treatmentNum = (JSONObject)jsonData.get("treatment");
                    userModel.setTreatmentTimes(treatmentNum.getString("number"));

                    jsonVision= (JSONObject)jsonData.get("vision");
                    userModel.setBeforeRight(jsonVision.getString("before_right"));
                    userModel.setBeforeLeft(jsonVision.getString("before_left"));
                    userModel.setNowNakedRight(jsonVision.getString("naked_right"));
                    userModel.setNowNakedLeft(jsonVision.getString("naked_left"));
                    userModel.setBeforeGlsRight(jsonVision.getString("before_gls_right"));
                    userModel.setBeforeGlsLeft(jsonVision.getString("before_gls_left"));
                    userModel.setNowGlsRight(jsonVision.getString("glasses_right"));
                    userModel.setNowGlsLeft(jsonVision.getString("glasses_left"));
                    Message message = Message.obtain();
                    message.what = 22222;
                    infoHandler.sendMessage(message);
                } catch (JSONException e) {}
            }
        });
    }

    /**
     *  返回按键检测
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment from=this.getSupportFragmentManager().findFragmentById(R.id.fl_HPFLayout);
        if(from instanceof CourseOfTreatmentFragment){
            ((CourseOfTreatmentFragment)from).onKeyDown(keyCode, event);
        }
        else if(from instanceof HealingProcessFragment)
        {
            ((HealingProcessFragment)from).onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

}
