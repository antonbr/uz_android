package com.uzapp.pojo.booking;

import com.google.gson.annotations.SerializedName;
import com.uzapp.pojo.prices.ServicesPrices;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Vladimir on 22.08.2016.
 */
@Parcel
public class Documents {

    @SerializedName("number")
    String number;
    @SerializedName("uid")
    String uid;
    @SerializedName("order_number")
    String orderNumber;
    @SerializedName("kind")
    String kind;
    @SerializedName("places_count")
    int placesCount;
    @SerializedName("places")
    List<Integer> placesList;
    @SerializedName("children_category")
    String childrenCategory;
    @SerializedName("children_date")
    int childrenDate;
    @SerializedName("services")
    List<ServicesPrices> servicesList;
    @SerializedName("firstname")
    String firstName;
    @SerializedName("lastname")
    String lastName;
    @SerializedName("middlename")
    String middleName;
    @SerializedName("birthday")
    int birthday;
    @SerializedName("privilege_category")
    String privilegeCategory;
    @SerializedName("privilege_serial")
    String privilegeSerial;
    @SerializedName("privilege_number")
    String privilegeNumber;
    @SerializedName("costs")
    List<Costs> costsList;
    @SerializedName("bonus_card_number")
    String bonusCardNumber;
    @SerializedName("bonus_type")
    String bonusType;
    @SerializedName("bonus_cost")
    String bonusCost;
    @SerializedName("insurance_company_address")
    String insuranceCompanyAddress;
    @SerializedName("insurance_company_name")
    String insuranceCompanyName;
    @SerializedName("insurance_company_telephone")
    String insuranceCompanyTelephone;

    public Documents() {}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getPlacesCount() {
        return placesCount;
    }

    public void setPlacesCount(int placesCount) {
        this.placesCount = placesCount;
    }

    public List<Integer> getPlacesList() {
        return placesList;
    }

    public void setPlacesList(List<Integer> placesList) {
        this.placesList = placesList;
    }

    public String getChildrenCategory() {
        return childrenCategory;
    }

    public void setChildrenCategory(String childrenCategory) {
        this.childrenCategory = childrenCategory;
    }

    public int getChildrenDate() {
        return childrenDate;
    }

    public void setChildrenDate(int childrenDate) {
        this.childrenDate = childrenDate;
    }

    public List<ServicesPrices> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<ServicesPrices> servicesList) {
        this.servicesList = servicesList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getPrivilegeCategory() {
        return privilegeCategory;
    }

    public void setPrivilegeCategory(String privilegeCategory) {
        this.privilegeCategory = privilegeCategory;
    }

    public String getPrivilegeSerial() {
        return privilegeSerial;
    }

    public void setPrivilegeSerial(String privilegeSerial) {
        this.privilegeSerial = privilegeSerial;
    }

    public String getPrivilegeNumber() {
        return privilegeNumber;
    }

    public void setPrivilegeNumber(String privilegeNumber) {
        this.privilegeNumber = privilegeNumber;
    }

    public List<Costs> getCostsList() {
        return costsList;
    }

    public void setCostsList(List<Costs> costsList) {
        this.costsList = costsList;
    }

    public String getBonusCardNumber() {
        return bonusCardNumber;
    }

    public void setBonusCardNumber(String bonusCardNumber) {
        this.bonusCardNumber = bonusCardNumber;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public String getBonusCost() {
        return bonusCost;
    }

    public void setBonusCost(String bonusCost) {
        this.bonusCost = bonusCost;
    }

    public String getInsuranceCompanyAddress() {
        return insuranceCompanyAddress;
    }

    public void setInsuranceCompanyAddress(String insuranceCompanyAddress) {
        this.insuranceCompanyAddress = insuranceCompanyAddress;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getInsuranceCompanyTelephone() {
        return insuranceCompanyTelephone;
    }

    public void setInsuranceCompanyTelephone(String insuranceCompanyTelephone) {
        this.insuranceCompanyTelephone = insuranceCompanyTelephone;
    }
}
