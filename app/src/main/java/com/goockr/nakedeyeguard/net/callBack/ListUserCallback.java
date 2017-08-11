package com.goockr.nakedeyeguard.net.callBack;

import android.util.Log;

import com.goockr.nakedeyeguard.data.Model.UserBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class ListUserCallback extends Callback<List<UserBean>>
{

    @Override
    public List<UserBean> parseNetworkResponse(Response response, int id) throws IOException
    {
        Log.d("ListUserCallback", "parseNetworkResponse: "+response);
        String string = response.body().string();
        List<UserBean> user = new Gson().fromJson(string,new TypeToken<List<UserBean>>(){}.getType() );
        return user;
    }


}
