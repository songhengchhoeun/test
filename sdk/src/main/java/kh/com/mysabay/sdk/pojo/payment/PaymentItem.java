package kh.com.mysabay.sdk.pojo.payment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Tan Phirum on 3/13/20
 * Gmail phirumtan@gmail.com
 */
public class PaymentItem implements Parcelable {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("hash")
    @Expose
    public String hash;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("asset_code")
    @Expose
    public String assetCode;
    public final static Parcelable.Creator<PaymentItem> CREATOR = new Creator<PaymentItem>() {


        @NotNull
        @Contract("_ -> new")
        public PaymentItem createFromParcel(Parcel in) {
            return new PaymentItem(in);
        }

        @NotNull
        @Contract(value = "_ -> new", pure = true)
        public PaymentItem[] newArray(int size) {
            return (new PaymentItem[size]);
        }

    };

    protected PaymentItem(@NotNull Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.hash = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((String) in.readValue((String.class.getClassLoader())));
        this.assetCode = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public PaymentItem() {
    }

    /**
     * @param amount
     * @param assetCode
     * @param message
     * @param hash
     * @param status
     */
    public PaymentItem(Integer status, String hash, String message, String amount, String assetCode) {
        super();
        this.status = status;
        this.hash = hash;
        this.message = message;
        this.amount = amount;
        this.assetCode = assetCode;
    }

    public PaymentItem withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public PaymentItem withHash(String hash) {
        this.hash = hash;
        return this;
    }

    public PaymentItem withMessage(String message) {
        this.message = message;
        return this;
    }

    public PaymentItem withAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public PaymentItem withAssetCode(String assetCode) {
        this.assetCode = assetCode;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("hash", hash).append("message", message).append("amount", amount).append("assetCode", assetCode).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(amount).append(assetCode).append(message).append(hash).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PaymentItem) == false) {
            return false;
        }
        PaymentItem rhs = ((PaymentItem) other);
        return new EqualsBuilder().append(amount, rhs.amount).append(assetCode, rhs.assetCode).append(message, rhs.message).append(hash, rhs.hash).append(status, rhs.status).isEquals();
    }

    public void writeToParcel(@NotNull Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(hash);
        dest.writeValue(message);
        dest.writeValue(amount);
        dest.writeValue(assetCode);
    }

    public int describeContents() {
        return 0;
    }

}