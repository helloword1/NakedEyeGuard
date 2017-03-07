package com.goockr.nakedeyeguard.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JJT-ssd on 2017/2/27.
 */

public class UserModel implements Parcelable{

    private String userName;
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    private String id;
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    private String userIconUrl;
    public String getUserIconUrl() {return userIconUrl;}
    public void setUserIconUrl(String userIconUrl) {this.userIconUrl = userIconUrl;}

    private String treatmentTimes;
    public String getTreatmentTimes() {return treatmentTimes;}
    public void setTreatmentTimes(String treatmentTimes) {this.treatmentTimes = treatmentTimes;}



    public UserModel(){}

    public UserModel(Parcel in) {
        userName = in.readString();
        id = in.readString();
        userIconUrl = in.readString();
        treatmentTimes= in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(id);
        dest.writeString(userIconUrl);
        dest.writeString(treatmentTimes);

    }

    @Override
    public int describeContents() {
        return 0;
    }

}
