package com.mifos.apache.fineract.data.models.rolesandpermission;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
public class Role implements Parcelable {

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("permissions")
    private List<Permission> permissions;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(
            List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.identifier);
        dest.writeTypedList(this.permissions);
    }

    public Role() {
    }

    protected Role(Parcel in) {
        this.identifier = in.readString();
        this.permissions = in.createTypedArrayList(Permission.CREATOR);
    }

    public static final Parcelable.Creator<Role> CREATOR = new Parcelable.Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel source) {
            return new Role(source);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };
}
