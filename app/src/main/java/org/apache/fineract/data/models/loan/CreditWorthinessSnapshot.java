package org.apache.fineract.data.models.loan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 12/07/17.
 */

public class CreditWorthinessSnapshot implements Parcelable {

    private String forCustomer;
    private List<CreditWorthinessFactor> incomeSources = new ArrayList<>();
    private List<CreditWorthinessFactor> assets = new ArrayList<>();
    private List<CreditWorthinessFactor> debts = new ArrayList<>();

    public String getForCustomer() {
        return forCustomer;
    }

    public void setForCustomer(String forCustomer) {
        this.forCustomer = forCustomer;
    }

    public List<CreditWorthinessFactor> getIncomeSources() {
        return incomeSources;
    }

    public void setIncomeSources(
            List<CreditWorthinessFactor> incomeSources) {
        this.incomeSources = incomeSources;
    }

    public List<CreditWorthinessFactor> getAssets() {
        return assets;
    }

    public void setAssets(
            List<CreditWorthinessFactor> assets) {
        this.assets = assets;
    }

    public List<CreditWorthinessFactor> getDebts() {
        return debts;
    }

    public void setDebts(List<CreditWorthinessFactor> debts) {
        this.debts = debts;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.forCustomer);
        dest.writeList(this.incomeSources);
        dest.writeList(this.assets);
        dest.writeList(this.debts);
    }

    public CreditWorthinessSnapshot() {
    }

    protected CreditWorthinessSnapshot(Parcel in) {
        this.forCustomer = in.readString();
        this.incomeSources = new ArrayList<CreditWorthinessFactor>();
        in.readList(this.incomeSources, CreditWorthinessFactor.class.getClassLoader());
        this.assets = new ArrayList<CreditWorthinessFactor>();
        in.readList(this.assets, CreditWorthinessFactor.class.getClassLoader());
        this.debts = new ArrayList<CreditWorthinessFactor>();
        in.readList(this.debts, CreditWorthinessFactor.class.getClassLoader());
    }

    public static final Parcelable.Creator<CreditWorthinessSnapshot> CREATOR =
            new Parcelable.Creator<CreditWorthinessSnapshot>() {
                @Override
                public CreditWorthinessSnapshot createFromParcel(Parcel source) {
                    return new CreditWorthinessSnapshot(source);
                }

                @Override
                public CreditWorthinessSnapshot[] newArray(int size) {
                    return new CreditWorthinessSnapshot[size];
                }
            };
}
