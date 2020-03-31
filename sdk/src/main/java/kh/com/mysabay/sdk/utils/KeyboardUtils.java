package kh.com.mysabay.sdk.utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by phirum on 1/13/17.
 */

public class KeyboardUtils {

    public static void hideKeyboard(Context context, View v) {
        try {
            if (v == null) return;
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Context context, View v) {
        try {
            if (v == null) return;
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(v, WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
