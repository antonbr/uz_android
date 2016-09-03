package com.uzapp.view.main.wagon.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.uzapp.pojo.prices.PlacesPrices;
import com.uzapp.pojo.prices.ServicesPrices;

import java.util.List;

/**
 * Created by Vladimir on 16.08.2016.
 */
public class Wagon implements Parcelable {

    private String charline;
    private String number;
    private String typeName;
    private String typeCode;
    private String countryName;
    private String countryCode;
    private String railwayName;
    private String railwayCode;
    private boolean sitting;
    private String className;
    private int classCode;
    private String costCurrency;
    private int cost;
    private String costReserveCurrency;
    private int costReserve;
    private boolean allowBonus;
    private List<ServicesPrices> services;
    private PlacesPrices placesPrices;
    private List<Integer> places;

    public Wagon() {}

    public String getCharline() {
        return charline;
    }

    public void setCharline(String charline) {
        this.charline = charline;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRailwayName() {
        return railwayName;
    }

    public void setRailwayName(String railwayName) {
        this.railwayName = railwayName;
    }

    public String getRailwayCode() {
        return railwayCode;
    }

    public void setRailwayCode(String railwayCode) {
        this.railwayCode = railwayCode;
    }

    public boolean isSitting() {
        return sitting;
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassCode() {
        return classCode;
    }

    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    public String getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(String costCurrency) {
        this.costCurrency = costCurrency;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCostReserveCurrency() {
        return costReserveCurrency;
    }

    public void setCostReserveCurrency(String costReserveCurrency) {
        this.costReserveCurrency = costReserveCurrency;
    }

    public int getCostReserve() {
        return costReserve;
    }

    public void setCostReserve(int costReserve) {
        this.costReserve = costReserve;
    }

    public boolean isAllowBonus() {
        return allowBonus;
    }

    public void setAllowBonus(boolean allowBonus) {
        this.allowBonus = allowBonus;
    }

    public List<ServicesPrices> getServices() {
        return services;
    }

    public void setServices(List<ServicesPrices> services) {
        this.services = services;
    }

    public PlacesPrices getPlacesPrices() {
        return placesPrices;
    }

    public void setPlacesPrices(PlacesPrices placesPrices) {
        this.placesPrices = placesPrices;
    }

    public List<Integer> getPlaces() {
        return places;
    }

    public void setPlaces(List<Integer> places) {
        this.places = places;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
