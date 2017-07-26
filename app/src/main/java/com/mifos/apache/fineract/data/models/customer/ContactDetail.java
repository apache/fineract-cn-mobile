package com.mifos.apache.fineract.data.models.customer;

import com.google.gson.annotations.SerializedName;

public final class ContactDetail {

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

    public String getType() {
        return this.type.name();
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
}