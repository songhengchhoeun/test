package kh.com.mysabay.sdk.callback;

/**
 * Created by Tan Phirum on 4/7/20
 * Gmail phirumtan@gmail.com
 */
public interface RefreshTokenListener {

    void refreshSuccess(String token);

    void refreshFailed(Throwable error);
}
