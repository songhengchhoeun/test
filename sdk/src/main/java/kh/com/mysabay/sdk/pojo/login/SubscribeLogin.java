package kh.com.mysabay.sdk.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tan Phirum on 3/27/20
 * Gmail phirumtan@gmail.com
 */
public class SubscribeLogin {
    @SerializedName("access_token")
    @Expose
    public final String accessToken;

    @SerializedName("error")
    @Expose
    public final Object error;

    public SubscribeLogin(String accessToken, Object error) {
        this.accessToken = accessToken;
        this.error = error;
    }
}
