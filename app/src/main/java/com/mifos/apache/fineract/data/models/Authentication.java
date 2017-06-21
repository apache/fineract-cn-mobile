package com.mifos.apache.fineract.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rajan Maurya
 *         On 17/06/17.
 */
public class Authentication implements Parcelable {

    @SerializedName("tokenType")
    String tokenType;

    @SerializedName("accessToken")
    String accessToken;

    @SerializedName("accessTokenExpiration")
    String accessTokenExpiration;

    @SerializedName("refreshTokenExpiration")
    String refreshTokenExpiration;

    @SerializedName("passwordExpiration")
    String passwordExpiration;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public void setAccessTokenExpiration(String accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(String refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String getPasswordExpiration() {
        return passwordExpiration;
    }

    public void setPasswordExpiration(String passwordExpiration) {
        this.passwordExpiration = passwordExpiration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenType);
        dest.writeString(this.accessToken);
        dest.writeString(this.accessTokenExpiration);
        dest.writeString(this.refreshTokenExpiration);
        dest.writeString(this.passwordExpiration);
    }

    public Authentication() {
    }

    protected Authentication(Parcel in) {
        this.tokenType = in.readString();
        this.accessToken = in.readString();
        this.accessTokenExpiration = in.readString();
        this.refreshTokenExpiration = in.readString();
        this.passwordExpiration = in.readString();
    }

    public static final Parcelable.Creator<Authentication> CREATOR =
            new Parcelable.Creator<Authentication>() {
                @Override
                public Authentication createFromParcel(Parcel source) {
                    return new Authentication(source);
                }

                @Override
                public Authentication[] newArray(int size) {
                    return new Authentication[size];
                }
            };
}