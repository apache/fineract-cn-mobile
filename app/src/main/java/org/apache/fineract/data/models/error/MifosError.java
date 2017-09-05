package org.apache.fineract.data.models.error;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rajan Maurya
 *         On 18/06/17.
 */

public class MifosError implements Parcelable {

    @SerializedName("timestamp")
    String timestamp;

    @SerializedName("status")
    Integer status;

    @SerializedName("error")
    String error;

    @SerializedName("message")
    String message;

    @SerializedName("path")
    String path;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.timestamp);
        dest.writeValue(this.status);
        dest.writeString(this.error);
        dest.writeString(this.message);
        dest.writeString(this.path);
    }

    public MifosError() {
    }

    protected MifosError(Parcel in) {
        this.timestamp = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.error = in.readString();
        this.message = in.readString();
        this.path = in.readString();
    }

    public static final Parcelable.Creator<MifosError> CREATOR =
            new Parcelable.Creator<MifosError>() {
                @Override
                public MifosError createFromParcel(Parcel source) {
                    return new MifosError(source);
                }

                @Override
                public MifosError[] newArray(int size) {
                    return new MifosError[size];
                }
            };
}
