package kh.com.mysabay.sdk.pojo.payment;

/**
 * Created by Tan Phirum on 3/28/20
 * Gmail phirumtan@gmail.com
 */
public class SubscribePayment {

    public final Object dataMySabay;
    public final Object dataIAP;
    public final Object dataError;

    public SubscribePayment(Object dataMySabay, Object dataIAP, Object dataError) {
        this.dataMySabay = dataMySabay;
        this.dataIAP = dataIAP;
        this.dataError = dataError;
    }
}
