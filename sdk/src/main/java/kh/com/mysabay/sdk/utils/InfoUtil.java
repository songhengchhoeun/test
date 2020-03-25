package kh.com.mysabay.sdk.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by Tan Phirum on 6/19/15.
 */
public class InfoUtil {

   /* public static String getDeviceImei(Context context) {
        String device_id = "";
        try {
            if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (manager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        device_id = manager.getImei();
                    } else
                        device_id = manager.getDeviceId();
                }
            }
            //in case if fail operation for some devices or tablets
            if (TextUtils.isEmpty(device_id)) device_id = getUniqueID(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return device_id;
    }*/

    public static String getUniqueID(Context activity) {
        String unique_id = "";
        try {
            unique_id = Settings.Secure.getString(activity.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unique_id;
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return WordUtils.capitalizeFully(model);
        } else {
            return WordUtils.capitalizeFully(manufacturer) + " " + model;
        }
    }
}
