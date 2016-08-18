package com.uzapp.pojo.placeslist;

import com.google.gson.annotations.SerializedName;
import com.uzapp.pojo.prices.ServicesPrices;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Vladimir on 01.08.2016.
 */
@Parcel
public class WagonsPlacesList {
    @SerializedName("charline")
    String charline;
    @SerializedName("number")
    String number;
    @SerializedName("type_name")
    String typeName;
    @SerializedName("type_code")
    String typeCode;
    @SerializedName("country_name")
    String countryName;
    @SerializedName("country_code")
    String countryCode;
    @SerializedName("railway_name")
    String railwayName;
    @SerializedName("railway_code")
    String railwayCode;
    @SerializedName("sitting")
    boolean sitting;
    @SerializedName("class_name")
    String className;
    @SerializedName("class_code")
    int classCode;
    @SerializedName("cost_currency")
    String costCurrency;
    @SerializedName("cost")
    int cost;
    @SerializedName("cost_reserve_currency")
    String costReserveCurrency;
    @SerializedName("cost_reserve")
    int costReserve;
    @SerializedName("allow_bonus")
    boolean allowBonus;
    @SerializedName("services")
    List<ServicesPrices> services;
    @SerializedName("places")
    List<Integer> places;

    public WagonsPlacesList() {

    }

    public WagonsPlacesList(String charline, String number, String typeName, String typeCode, String countryName,
                            String countryCode, String railwayName, String railwayCode, boolean sitting, String className,
                            int classCode, String costCurrency, int cost, String costReserveCurrency, int costReserve,
                            boolean allowBonus, List<ServicesPrices> services, List<Integer> places) {
        this.charline = charline;
        this.number = number;
        this.typeName = typeName;
        this.typeCode = typeCode;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.railwayName = railwayName;
        this.railwayCode = railwayCode;
        this.sitting = sitting;
        this.className = className;
        this.classCode = classCode;
        this.costCurrency = costCurrency;
        this.cost = cost;
        this.costReserveCurrency = costReserveCurrency;
        this.costReserve = costReserve;
        this.allowBonus = allowBonus;
        this.services = services;
        this.places = places;
    }

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

    public void setTypeCode(String type_code) {
        this.typeCode = type_code;
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

    public List<Integer> getPlaces() {
        return places;
    }

    public void setPlaces(List<Integer> places) {
        this.places = places;
    }
}
