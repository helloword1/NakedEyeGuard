package com.goockr.nakedeyeguard.net.callBack;

import com.goockr.nakedeyeguard.data.Model.UserDetailBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class UserCallback extends Callback<UserDetailBean>
{
    @Override
    public UserDetailBean parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        UserDetailBean user = new Gson().fromJson(string, UserDetailBean.class);
        return user;
    }


}
