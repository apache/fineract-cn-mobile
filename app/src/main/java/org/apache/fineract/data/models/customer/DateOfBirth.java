package org.apache.fineract.data.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

public final class DateOfBirth implements Parcelable {

    private Integer year;
    private Integer month;
    private Integer day;

    public DateOfBirth() {
        super();
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(final Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(final Integer day) {
        this.day = day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.year);
        dest.writeValue(this.month);
        dest.writeValue(this.day);
    }

    protected DateOfBirth(Parcel in) {
        this.year = (Integer) in.readValue(Integer.class.getClassLoader());
        this.month = (Integer) in.readValue(Integer.class.getClassLoader());
        this.day = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<DateOfBirth> CREATOR =
            new Parcelable.Creator<DateOfBirth>() {
                @Override
                public DateOfBirth createFromParcel(Parcel source) {
                    return new DateOfBirth(source);
                }

                @Override
                public DateOfBirth[] newArray(int size) {
                    return new DateOfBirth[size];
                }
            };
}