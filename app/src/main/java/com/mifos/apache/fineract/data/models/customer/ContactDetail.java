package com.mifos.apache.fineract.data.models.customer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public final class ContactDetail implements Parcelable {

    public enum Type {
        @SerializedName("EMAIL")
        EMAIL,

        @SerializedName("PHONE")
        PHONE,

        @SerializedName("MOBILE")
        MOBILE
    }

    public enum Group {

        @SerializedName("BUSINESS")
        BUSINESS,

        @SerializedName("PRIVATE")
        PRIVATE
    }

    private Type type;
    private Group group;
    private String value;
    private Integer preferenceLevel;
    private Boolean validated;

    public ContactDetail() {
        super();
    }

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public String getGroup() {
        return this.group.name();
    }

    public void setGroup(final Group group) {
        this.group = group;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Boolean getValidated() {
        return this.validated;
    }

    public void setValidated(final Boolean validated) {
        this.validated = validated;
    }

    public Integer getPreferenceLevel() {
        return this.preferenceLevel;
    }

    public void setPreferenceLevel(final Integer preferenceLevel) {
        this.preferenceLevel = preferenceLevel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.group == null ? -1 : this.group.ordinal());
        dest.writeString(this.value);
        dest.writeValue(this.preferenceLevel);
        dest.writeValue(this.validated);
    }

    protected ContactDetail(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        int tmpGroup = in.readInt();
        this.group = tmpGroup == -1 ? null : Group.values()[tmpGroup];
        this.value = in.readString();
        this.preferenceLevel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.validated = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ContactDetail> CREATOR =
            new Parcelable.Creator<ContactDetail>() {
                @Override
                public ContactDetail createFromParcel(Parcel source) {
                    return new ContactDetail(source);
                }

                @Override
                public ContactDetail[] newArray(int size) {
                    return new ContactDetail[size];
                }
            };
}