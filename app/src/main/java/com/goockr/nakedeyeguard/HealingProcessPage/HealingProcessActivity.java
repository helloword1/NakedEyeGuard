package com.goockr.nakedeyeguard.HealingProcessPage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import static com.goockr.nakedeyeguard.Tools.Common.replaFragment;

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

        replaFragment(this,new HealingProcessFragment(),R.id.fl_HPFLayout,false);

    }


    protected void setupUI() {

    }
    public void setHandler(Handler handler) {
        infoHandler = handler;
    }

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

}
