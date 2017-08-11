package com.goockr.nakedeyeguard.net.callBack;

import com.goockr.nakedeyeguard.data.Model.TreatmentBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class TreatmentCallback extends Callback<TreatmentBean>
{
    @Override
    public TreatmentBean parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        TreatmentBean user = new Gson().fromJson(string, TreatmentBean.class);
        return user;
    }


}
