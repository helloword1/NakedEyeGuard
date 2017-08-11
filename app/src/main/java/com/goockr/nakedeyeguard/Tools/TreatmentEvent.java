package com.goockr.nakedeyeguard.Tools;

import android.provider.ContactsContract;

import org.json.JSONObject;

import java.util.Date;
import java.util.Observable;

/**
 * Created by CMQ on 2017/6/6.
 */

public class TreatmentEvent  extends Observable {

    private volatile static TreatmentEvent instance;

    public static TreatmentEvent getInstance(){
        if (instance == null) {
            synchronized (TreatmentEvent.class) {
                if (instance == null) {
                    instance = new TreatmentEvent();
                }
            }
        }
        return instance;
    }

    public void postApproveNoti( int object)
    {
        setChanged();

        notifyObservers(object);
    }

    public void clear(){
        instance = null;
    }


}
