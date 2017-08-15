package com.mifos.apache.fineract.data.models.deposit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 07/07/17.
 */
public class CustomerDepositAccounts implements Parcelable {

    private String customerIdentifier;
    private String productIdentifier;
    private String accountIdentifier;
    private List<String> beneficiaries = new ArrayList<>();
    private State state;
    private Double balance;

    public enum State {

        @SerializedName("CREATED")
        CREATED,

        @SerializedName("PENDING")
        PENDING,

        @SerializedName("APPROVED")
        APPROVED,

        @SerializedName("ACTIVE")
        ACTIVE,

        @SerializedName("LOCKED")
        LOCKED,

        @SerializedName("CLOSED")
        CLOSED
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public List<String> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<String> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customerIdentifier);
        dest.writeString(this.productIdentifier);
        dest.writeString(this.accountIdentifier);
        dest.writeStringList(this.beneficiaries);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeValue(this.balance);
    }

    public CustomerDepositAccounts() {
    }

    protected CustomerDepositAccounts(Parcel in) {
        this.customerIdentifier = in.readString();
        this.productIdentifier = in.readString();
        this.accountIdentifier = in.readString();
        this.beneficiaries = in.createStringArrayList();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : State.values()[tmpState];
        this.balance = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<CustomerDepositAccounts> CREATOR =
            new Parcelable.Creator<CustomerDepositAccounts>() {
                @Override
                public CustomerDepositAccounts createFromParcel(Parcel source) {
                    return new CustomerDepositAccounts(source);
                }

                @Override
                public CustomerDepositAccounts[] newArray(int size) {
                    return new CustomerDepositAccounts[size];
                }
            };
}
