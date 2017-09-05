package org.apache.fineract.data.models.customer.identification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rajan Maurya
 *         On 01/08/17.
 */

public class ScanCard implements Parcelable {

    @SerializedName("description")
    String description;

    @SerializedName("identifier")
    String identifier;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.identifier);
    }

    public ScanCard() {
    }

    protected ScanCard(Parcel in) {
        this.description = in.readString();
        this.identifier = in.readString();
    }

    public static final Parcelable.Creator<ScanCard> CREATOR = new Parcelable.Creator<ScanCard>() {
        @Override
        public ScanCard createFromParcel(Parcel source) {
            return new ScanCard(source);
        }

        @Override
        public ScanCard[] newArray(int size) {
            return new ScanCard[size];
        }
    };
}
