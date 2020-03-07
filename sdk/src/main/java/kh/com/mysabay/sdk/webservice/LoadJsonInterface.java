package kh.com.mysabay.sdk.webservice;

import android.content.Context;

import io.reactivex.Flowable;

/**
 * Created by Tan Phirum
 * Gmail phirumtan@gmail.com
 * on 10/4/18.
 */
public interface LoadJsonInterface {

    Flowable<String> loadJson(Context context, String path);
}
