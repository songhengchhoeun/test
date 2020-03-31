package kh.com.mysabay.sdk.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tan Phirum on 3/9/20
 * Gmail phirumtan@gmail.com
 */
public class AppItem implements Parcelable {

    @SerializedName("app_secret")
    @Expose
    public String appSecret;

    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("uuid")
    @Expose
    public String uuid;

    public AppItem(String appSecret, String token, String uuid) {
        this.appSecret = appSecret;
        this.token = token;
        this.uuid = uuid;
    }

    public AppItem(String appSecret, String token) {
        this(appSecret, token, null);
    }

    public AppItem(String appSecret) {
        this(appSecret, null, null);
    }

    protected AppItem(Parcel in) {
        appSecret = in.readString();
        token = in.readString();
        uuid = in.readString();
    }

    public static final Creator<AppItem> CREATOR = new Creator<AppItem>() {
        @Override
        public AppItem createFromParcel(Parcel in) {
            return new AppItem(in);
        }

        @Override
        public AppItem[] newArray(int size) {
            return new AppItem[size];
        }
    };

    public AppItem withAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public AppItem withToken(String token) {
        this.token = token;
        return this;
    }

    public AppItem withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appSecret);
        dest.writeString(token);
        dest.writeString(uuid);
    }
}
