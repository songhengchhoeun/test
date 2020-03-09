package kh.com.mysabay.sdk.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tan Phirum on 3/9/20
 * Gmail phirumtan@gmail.com
 */
public class AppRequiredItem implements Parcelable {

    @SerializedName("app_secret")
    @Expose
    public String appSecret;

    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("uuid")
    @Expose
    public String uuid;

    public AppRequiredItem(String appSecret, String token, String uuid) {
        this.appSecret = appSecret;
        this.token = token;
        this.uuid = uuid;
    }

    public AppRequiredItem(String appSecret, String token) {
        this(appSecret, token, null);
    }

    public AppRequiredItem(String appSecret) {
        this(appSecret, null, null);
    }

    protected AppRequiredItem(Parcel in) {
        appSecret = in.readString();
        token = in.readString();
        uuid = in.readString();
    }

    public static final Creator<AppRequiredItem> CREATOR = new Creator<AppRequiredItem>() {
        @Override
        public AppRequiredItem createFromParcel(Parcel in) {
            return new AppRequiredItem(in);
        }

        @Override
        public AppRequiredItem[] newArray(int size) {
            return new AppRequiredItem[size];
        }
    };

    public AppRequiredItem withAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public AppRequiredItem withToken(String token) {
        this.token = token;
        return this;
    }

    public AppRequiredItem withUuid(String uuid) {
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
