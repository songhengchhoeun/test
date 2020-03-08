package kh.com.mysabay.sdk.utils;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Tan Phirum on 3/12/18.
 */
public class AnimationUtils {

    public static void playShake(View v) {
        playShake(v, 600);
    }

    public static void playShake(View v, int duration) {
        YoYo.with(Techniques.Shake).duration(duration).playOn(v);
    }
}
