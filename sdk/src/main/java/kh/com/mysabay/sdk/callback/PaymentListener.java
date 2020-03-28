package kh.com.mysabay.sdk.callback;

/**
 * Created by Tan Phirum on 3/28/20
 * Gmail phirumtan@gmail.com
 */
public interface PaymentListener {

    void purchaseMySabaySuccess(Object dataMySabay);

    void purchaseAIPSuccess(Object dataAIP);

    void purchaseFailed(Object dataError);
}
