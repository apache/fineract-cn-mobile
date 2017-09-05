package org.apache.fineract.data.models.loan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public class CreditWorthinessFactor implements Parcelable {

    private String description;
    private Double amount;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeValue(this.amount);
    }

    public CreditWorthinessFactor() {
    }

    protected CreditWorthinessFactor(Parcel in) {
        this.description = in.readString();
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<CreditWorthinessFactor> CREATOR =
            new Parcelable.Creator<CreditWorthinessFactor>() {
                @Override
                public CreditWorthinessFactor createFromParcel(Parcel source) {
                    return new CreditWorthinessFactor(source);
                }

                @Override
                public CreditWorthinessFactor[] newArray(int size) {
                    return new CreditWorthinessFactor[size];
                }
            };
}
