package kh.com.mysabay.sdk.webservice;

import android.content.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.reactivex.Flowable;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by Tan Phirum
 * Gmail phirumtan@gmail.com
 * on 10/4/18.
 */
public class LoadJsonInterfaceStream implements LoadJsonInterface {
    @Override
    public Flowable<String> loadJson(Context context, String path) {
        return Flowable.generate(() -> context.getAssets().open(path), (inputStream, output) -> {
            int size = inputStream.available();
            if (size <= 0) {
                output.onComplete();
            } else {
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                output.onNext(new String(buffer, StandardCharsets.UTF_8));
            }
            return inputStream;
        }, inputStream -> {
            try {
                inputStream.close();
            } catch (IOException ex) {
                RxJavaPlugins.onError(ex);
            }

        });
    }

}
