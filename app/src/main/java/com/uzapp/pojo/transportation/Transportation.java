package com.uzapp.pojo.transportation;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.uzapp.pojo.booking.Documents;

import org.parceler.Parcel;

/**
 * Created by Vladimir on 09.09.2016.
 */
@Parcel
public class Transportation implements Parcelable {

    @SerializedName("electronic")
    boolean electronic;
    @SerializedName("uio")
    String uio;
    @SerializedName("wait_seconds")
    int waitSeconds;
    @SerializedName("sys_date")
    int sysDate;
    @SerializedName("document")
    Documents documentsList;

    public Transportation() {}

    protected Transportation(android.os.Parcel in) {
        electronic = in.readByte() != 0;
        uio = in.readString();
        waitSeconds = in.readInt();
        sysDate = in.readInt();
    }

    public static final Creator<Transportation> CREATOR = new Creator<Transportation>() {
        @Override
        public Transportation createFromParcel(android.os.Parcel in) {
            return new Transportation(in);
        }

        @Override
        public Transportation[] newArray(int size) {
            return new Transportation[size];
        }
    };

    public boolean isElectronic() {
        return electronic;
    }

    public void setElectronic(boolean electronic) {
        this.electronic = electronic;
    }

    public String getUio() {
        return uio;
    }

    public void setUio(String uio) {
        this.uio = uio;
    }

    public int isWaitSeconds() {
        return waitSeconds;
    }

    public void setWaitSeconds(int waitSeconds) {
        this.waitSeconds = waitSeconds;
    }

    public int isSysDate() {
        return sysDate;
    }

    public void setSysDate(int sysDate) {
        this.sysDate = sysDate;
    }

    public Documents getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(Documents documentsList) {
        this.documentsList = documentsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeByte((byte) (electronic ? 1 : 0));
        dest.writeString(uio);
        dest.writeInt(waitSeconds);
        dest.writeInt(sysDate);
    }
}
