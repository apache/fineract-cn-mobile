package com.mifos.apache.fineract.data.models.customer.identification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class Identification implements Parcelable {

    @SerializedName("type")
    String type;

    @SerializedName("number")
    String number;

    @SerializedName("expirationDate")
    ExpirationDate expirationDate;

    @SerializedName("issuer")
    String issuer;

    @SerializedName("createdBy")
    String createdBy;

    @SerializedName("createdOn")
    String createdOn;

    @SerializedName("lastModifiedBy")
    String lastModifiedBy;

    @SerializedName("lastModifiedOn")
    String lastModifiedOn;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ExpirationDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(
            ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public static Creator<Identification> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.number);
        dest.writeParcelable(this.expirationDate, flags);
        dest.writeString(this.issuer);
        dest.writeString(this.createdBy);
        dest.writeString(this.createdOn);
        dest.writeString(this.lastModifiedBy);
        dest.writeString(this.lastModifiedOn);
    }

    public Identification() {
    }

    protected Identification(Parcel in) {
        this.type = in.readString();
        this.number = in.readString();
        this.expirationDate = in.readParcelable(ExpirationDate.class.getClassLoader());
        this.issuer = in.readString();
        this.createdBy = in.readString();
        this.createdOn = in.readString();
        this.lastModifiedBy = in.readString();
        this.lastModifiedOn = in.readString();
    }

    public static final Parcelable.Creator<Identification> CREATOR =
            new Parcelable.Creator<Identification>() {
                @Override
                public Identification createFromParcel(Parcel source) {
                    return new Identification(source);
                }

                @Override
                public Identification[] newArray(int size) {
                    return new Identification[size];
                }
            };
}
