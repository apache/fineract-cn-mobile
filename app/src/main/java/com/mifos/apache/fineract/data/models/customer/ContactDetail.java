package com.mifos.apache.fineract.data.models.customer;

public final class ContactDetail {

    public enum Type {
        EMAIL,
        PHONE,
        MOBILE
    }

    public enum Group {
        BUSINESS,
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

    public void setType(final String type) {
        this.type = Type.valueOf(type.toUpperCase());
    }

    public String getValue() {
        return this.value;
    }

    public String getGroup() {
        return this.group.name();
    }

    public void setGroup(final String group) {
        this.group = Group.valueOf(group);
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