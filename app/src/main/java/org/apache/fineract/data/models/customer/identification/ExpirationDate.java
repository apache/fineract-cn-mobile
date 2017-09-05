package org.apache.fineract.data.models.customer.identification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Rajan Maurya
 *         On 31/07/17.
 */
public class ExpirationDate implements Parcelable {

    private Integer year;
    private Integer month;
    private Integer day;

    public ExpirationDate() {
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

    protected ExpirationDate(Parcel in) {
        this.year = (Integer) in.readValue(Integer.class.getClassLoader());
        this.month = (Integer) in.readValue(Integer.class.getClassLoader());
        this.day = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ExpirationDate> CREATOR =
            new Parcelable.Creator<ExpirationDate>() {
                @Override
                public ExpirationDate createFromParcel(Parcel source) {
                    return new ExpirationDate(source);
                }

                @Override
                public ExpirationDate[] newArray(int size) {
                    return new ExpirationDate[size];
                }
            };
}
