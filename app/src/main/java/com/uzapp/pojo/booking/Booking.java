package com.uzapp.pojo.booking;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Vladimir on 22.08.2016.
 */
@Parcel
public class Booking implements Parcelable {

    @SerializedName("uio")
    String uio;
    @SerializedName("wait_seconds")
    int waitSeconds;
    @SerializedName("station_from_code")
    int stationFromCode;
    @SerializedName("station_from_name")
    String stationFromName;
    @SerializedName("station_to_code")
    int stationToCode;
    @SerializedName("station_to_name")
    String stationToName;
    @SerializedName("date_from")
    int dateFrom;
    @SerializedName("date_to")
    int dateTo;
    @SerializedName("electronic")
    String electronic;
    @SerializedName("train_number")
    String trainNumber;
    @SerializedName("train_model")
    int trainModel;
    @SerializedName("train_class_code")
    String trainClassCode;
    @SerializedName("train_fasted_code")
    int trainFastedCode;
    @SerializedName("wagon_type")
    String wagonType;
    @SerializedName("wagon_class")
    int wagonClass;
    @SerializedName("wagon_number")
    int wagonNumber;
    @SerializedName("sys_date")
    int sysDate;
    @SerializedName("documents")
    List<Documents> documentsList;

    public Booking() {}

    protected Booking(android.os.Parcel in) {
        uio = in.readString();
        waitSeconds = in.readInt();
        stationFromCode = in.readInt();
        stationFromName = in.readString();
        stationToCode = in.readInt();
        stationToName = in.readString();
        dateFrom = in.readInt();
        dateTo = in.readInt();
        electronic = in.readString();
        trainNumber = in.readString();
        trainModel = in.readInt();
        trainClassCode = in.readString();
        trainFastedCode = in.readInt();
        wagonType = in.readString();
        wagonClass = in.readInt();
        wagonNumber = in.readInt();
        sysDate = in.readInt();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(android.os.Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public String getUio() {
        return uio;
    }

    public void setUio(String uio) {
        this.uio = uio;
    }

    public int getWaitSeconds() {
        return waitSeconds;
    }

    public void setWaitSeconds(int waitSeconds) {
        this.waitSeconds = waitSeconds;
    }

    public int getStationFromCode() {
        return stationFromCode;
    }

    public void setStationFromCode(int stationFromCode) {
        this.stationFromCode = stationFromCode;
    }

    public String getStationFromName() {
        return stationFromName;
    }

    public void setStationFromName(String stationFromName) {
        this.stationFromName = stationFromName;
    }

    public int getStationToCode() {
        return stationToCode;
    }

    public void setStationToCode(int stationToCode) {
        this.stationToCode = stationToCode;
    }

    public String getStationToName() {
        return stationToName;
    }

    public void setStationToName(String stationToName) {
        this.stationToName = stationToName;
    }

    public int getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(int dateFrom) {
        this.dateFrom = dateFrom;
    }

    public int getDateTo() {
        return dateTo;
    }

    public void setDateTo(int dateTo) {
        this.dateTo = dateTo;
    }

    public String getElectronic() {
        return electronic;
    }

    public void setElectronic(String electronic) {
        this.electronic = electronic;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public int getTrainModel() {
        return trainModel;
    }

    public void setTrainModel(int trainModel) {
        this.trainModel = trainModel;
    }

    public String getTrainClassCode() {
        return trainClassCode;
    }

    public void setTrainClassCode(String trainClassCode) {
        this.trainClassCode = trainClassCode;
    }

    public int getTrainFastedCode() {
        return trainFastedCode;
    }

    public void setTrainFastedCode(int trainFastedCode) {
        this.trainFastedCode = trainFastedCode;
    }

    public String getWagonType() {
        return wagonType;
    }

    public void setWagonType(String wagonType) {
        this.wagonType = wagonType;
    }

    public int getWagonClass() {
        return wagonClass;
    }

    public void setWagonClass(int wagonClass) {
        this.wagonClass = wagonClass;
    }

    public int getWagonNumber() {
        return wagonNumber;
    }

    public void setWagonNumber(int wagonNumber) {
        this.wagonNumber = wagonNumber;
    }

    public int getSysDate() {
        return sysDate;
    }

    public void setSysDate(int sysDate) {
        this.sysDate = sysDate;
    }

    public List<Documents> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(uio);
        dest.writeInt(waitSeconds);
        dest.writeInt(stationFromCode);
        dest.writeString(stationFromName);
        dest.writeInt(stationToCode);
        dest.writeString(stationToName);
        dest.writeInt(dateFrom);
        dest.writeInt(dateTo);
        dest.writeString(electronic);
        dest.writeString(trainNumber);
        dest.writeInt(trainModel);
        dest.writeString(trainClassCode);
        dest.writeInt(trainFastedCode);
        dest.writeString(wagonType);
        dest.writeInt(wagonClass);
        dest.writeInt(wagonNumber);
        dest.writeInt(sysDate);
    }
}
