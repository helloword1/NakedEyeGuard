package com.goockr.nakedeyeguard.data.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JJT-ssd on 2017/2/27.
 * 用户模型
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

    //治疗的次数
    private String treatmentTimes;
    public String getTreatmentTimes() {return treatmentTimes;}
    public void setTreatmentTimes(String treatmentTimes) {this.treatmentTimes = treatmentTimes;}

    //治疗方案 A B C
    private String treatmentTypes;
    public String getTreatmentTypes() {return treatmentTypes;}
    public void setTreatmentTypes(String treatmentTypes) {this.treatmentTypes = treatmentTypes;}



    private int remainingNumber=0;
    public int getRemainingNumber() {return remainingNumber;}
    public void setRemainingNumber(int remainingNumber) {this.remainingNumber = remainingNumber;}

    public String getBeforeRight() {
        return beforeRight;
    }
    public void setBeforeRight(String beforeRight) {
        this.beforeRight = beforeRight;
    }



    public String getNowGlsLeft() {
        return nowGlsLeft;
    }
    public void setNowGlsLeft(String nowGlsLeft) {
        this.nowGlsLeft = nowGlsLeft;
    }

    public String getNowGlsRight() {return nowGlsRight;}
    public void setNowGlsRight(String nowGlsRight) {
        this.nowGlsRight = nowGlsRight;
    }

    public String getNowNakedLeft() {return nowNakedLeft;}

    public void setNowNakedLeft(String nowNakedLeft) {
        this.nowNakedLeft = nowNakedLeft;
    }

    public String getNowNakedRight() {return nowNakedRight;}

    public void setNowNakedRight(String nowNakedRight) {
        this.nowNakedRight = nowNakedRight;
    }

    public String getBeforeGlsLeft() {return beforeGlsLeft;}

    public void setBeforeGlsLeft(String beforeGlsLeft) {
        this.beforeGlsLeft = beforeGlsLeft;
    }

    public String getBeforeGlsRight() {

        return beforeGlsRight;
    }

    public void setBeforeGlsRight(String beforeGlsRight) {
        this.beforeGlsRight = beforeGlsRight;
    }

    public String getBeforeLeft() {return beforeLeft;}

    public void setBeforeLeft(String beforeLeft) {
        this.beforeLeft = beforeLeft;
    }
    private String beforeRight="--";//原裸眼右视力
    private String beforeLeft="--";//原裸眼左视力
    private String beforeGlsRight="--";//原戴镜右视力
    private String beforeGlsLeft="--";//原戴镜左视力
    private String nowNakedRight="--";//现裸眼右视力
    private String nowNakedLeft="--";//现裸眼左视力
    private String nowGlsRight="--";//现戴镜右视力
    private String nowGlsLeft="--";//现戴镜左视力

    private String programme;
    public String getProgramme() {return programme;}
    public void setProgramme(String programme) {this.programme = programme;}

    public UserModel(){}

    public UserModel(Parcel in) {
        userName = in.readString();
        id = in.readString();
        userIconUrl = in.readString();
        treatmentTimes= in.readString();
        beforeRight= in.readString();
        beforeLeft= in.readString();
        beforeGlsRight= in.readString();
        beforeGlsLeft= in.readString();
        nowNakedRight= in.readString();
        nowNakedLeft= in.readString();
        nowGlsRight= in.readString();
        nowGlsLeft= in.readString();
        treatmentTypes= in.readString();;
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

        dest.writeString(beforeRight);
        dest.writeString(beforeLeft);
        dest.writeString(beforeGlsRight);
        dest.writeString(beforeGlsLeft);
        dest.writeString(nowNakedRight);
        dest.writeString(nowNakedLeft);
        dest.writeString(nowGlsRight);
        dest.writeString(nowGlsLeft);
        dest.writeString(treatmentTypes);
    }

    @Override
    public int describeContents() {
        return 0;
    }



}
