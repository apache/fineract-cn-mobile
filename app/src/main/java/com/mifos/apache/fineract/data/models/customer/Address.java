package com.mifos.apache.fineract.data.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

public final class Address implements Parcelable {
    private String street;
    private String city;
    private String region;
    private String postalCode;
    private String countryCode;
    private String country;

    public Address() {
        super();
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.street);
        dest.writeString(this.city);
        dest.writeString(this.region);
        dest.writeString(this.postalCode);
        dest.writeString(this.countryCode);
        dest.writeString(this.country);
    }

    protected Address(Parcel in) {
        this.street = in.readString();
        this.city = in.readString();
        this.region = in.readString();
        this.postalCode = in.readString();
        this.countryCode = in.readString();
        this.country = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}