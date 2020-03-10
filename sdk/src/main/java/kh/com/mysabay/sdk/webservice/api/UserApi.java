package kh.com.mysabay.sdk.webservice.api;

import io.reactivex.Observable;
import kh.com.mysabay.sdk.pojo.login.LoginItem;
import kh.com.mysabay.sdk.pojo.profile.UserProfileItem;
import kh.com.mysabay.sdk.pojo.verified.VerifiedItem;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Tan Phirum on 3/7/20
 * Gmail phirumtan@gmail.com
 */
public interface UserApi {

    @GET("api/v1/user/login/{phone}")
    Observable<LoginItem> getUserLogin(@Header("app_secret") String appSecret, @Path("phone") String phone);

    @FormUrlEncoded
    @POST("api/v1/user/verify_code")
    Observable<VerifiedItem> postVerifyCode(@Header("app_secret") String appSecret, @Header("Authorization") String token,
                                            @Field("phone") String phone, @Field("verify_code") int verifyCode);

    @Headers({
            "Accept: */*"
    })
    @POST("api/v1/user/mysabay/login")
    Observable<String> postLoginWithMySabay(@Header("app_secret") String appSecret);

    @GET("api/v1/user/profile")
    Observable<UserProfileItem> getUserProfile(@Header("app_secret") String appSecret, @Header("Authorization") String token);

}
