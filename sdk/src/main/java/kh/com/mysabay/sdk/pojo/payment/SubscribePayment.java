package kh.com.mysabay.sdk.pojo.payment;

/**
 * Created by Tan Phirum on 3/28/20
 * Gmail phirumtan@gmail.com
 */
public class SubscribePayment {

    public final Object dataMySabay;
    public final Object dataAIP;
    public final Object dataError;

    public SubscribePayment(Object dataMySabay, Object dataAIP, Object dataError) {
        this.dataMySabay = dataMySabay;
        this.dataAIP = dataAIP;
        this.dataError = dataError;
    }
}
