package com.goockr.nakedeyeguard.data.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CPF on 2017/6/26.
 */

public class UserBean implements Parcelable{
    private String name;
    private String head_image;
    private String id;

    protected UserBean(Parcel in) {
        name = in.readString();
        head_image = in.readString();
        id = in.readString();
    }

    public UserBean() {
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(head_image);
        dest.writeString(id);
    }
}
