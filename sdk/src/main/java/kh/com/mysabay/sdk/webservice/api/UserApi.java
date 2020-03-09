package kh.com.mysabay.sdk.webservice.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Tan Phirum on 3/7/20
 * Gmail phirumtan@gmail.com
 */
public interface UserApi {

    @GET("api/user/login/{phone}")
    Observable<Object> getUserLogin(@Header("app_secret") String appSecret, @Path("phone") String phone);

    @FormUrlEncoded
    @POST("api/user/verify_code")
    Observable<Object> postVerifyCode(@Header("app_secret") String appSecret, @Header("Authorization") String token,
                                      @Field("phone") String phone, @Field("verify_code") int verifyCode);

    @FormUrlEncoded
    @POST("api/user/mysabay/login")
    Observable<Object> postLoginWithMySabay(@Header("app_secret") String appSecret);

    @GET("api/user/profile")
    Observable<Object> getUserProfile(@Header("app_secret") String appSecret, @Header("Authorization") String token);

}
