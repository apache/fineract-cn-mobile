package com.mifos.apache.fineract.data.models.rolesandpermission;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 24/08/17.
 */
public class Permission implements Parcelable {

    @SerializedName("permittableEndpointGroupIdentifier")
    private String permittableEndpointGroupIdentifier;

    @SerializedName("allowedOperations")
    private List<AllowedOperation> allowedOperations;

    public String getPermittableEndpointGroupIdentifier() {
        return permittableEndpointGroupIdentifier;
    }

    public void setPermittableEndpointGroupIdentifier(String permittableEndpointGroupIdentifier) {
        this.permittableEndpointGroupIdentifier = permittableEndpointGroupIdentifier;
    }

    public List<AllowedOperation> getAllowedOperations() {
        return allowedOperations;
    }

    public void setAllowedOperations(
            List<AllowedOperation> allowedOperations) {
        this.allowedOperations = allowedOperations;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.permittableEndpointGroupIdentifier);
        dest.writeList(this.allowedOperations);
    }

    public Permission() {
    }

    protected Permission(Parcel in) {
        this.permittableEndpointGroupIdentifier = in.readString();
        this.allowedOperations = new ArrayList<AllowedOperation>();
        in.readList(this.allowedOperations, AllowedOperation.class.getClassLoader());
    }

    public static final Parcelable.Creator<Permission> CREATOR =
            new Parcelable.Creator<Permission>() {
                @Override
                public Permission createFromParcel(Parcel source) {
                    return new Permission(source);
                }

                @Override
                public Permission[] newArray(int size) {
                    return new Permission[size];
                }
            };
}
