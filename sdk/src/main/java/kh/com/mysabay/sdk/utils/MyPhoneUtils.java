package kh.com.mysabay.sdk.utils;

import android.content.Context;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import kh.com.mysabay.sdk.R;

public class MyPhoneUtils {

    public static String formatPhoneKh(Context context, String phoneNo) {
        if (isValidatePhone(phoneNo)) {
            return phoneValidation(context, phoneNo, "KH");
        } else {
            MessageUtil.displayToast(context, R.string.msg_phone_incorrect);
            return phoneNo;
        }
    }

    public static String phoneValidation(Context context, String phoneNo, String region) {
        PhoneNumberUtil phoneUtil = getPhoneNumberUtil();
        try {
            Phonenumber.PhoneNumber phone = phoneUtil.parse(phoneNo, region);
            return phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            e.printStackTrace();
            MessageUtil.displayToast(context, "error " + e.getLocalizedMessage());
            return "";
        }
    }

    public static Phonenumber.PhoneNumber phoneNumber(Context context, String phoneNo, String region) {
        PhoneNumberUtil phoneUtil = getPhoneNumberUtil();
        Phonenumber.PhoneNumber phone;
        try {
            phone = phoneUtil.parse(phoneNo, region);
            return phone;
        } catch (NumberParseException e) {
            e.printStackTrace();
            return new Phonenumber.PhoneNumber();
        }
    }

    public static Phonenumber.PhoneNumber phoneNumber(String phoneNo) {
        return phoneNumber(null, phoneNo, "KH");
    }

    private static PhoneNumberUtil getPhoneNumberUtil() {
        return PhoneNumberUtil.getInstance();
    }

    public static boolean isValidatePhone(String phoneNo) {
        return getPhoneNumberUtil().isValidNumber(phoneNumber(phoneNo));
    }
}
