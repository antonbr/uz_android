package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uzapp.pojo.TicketKind;
import com.uzapp.pojo.WagonClass;
import com.uzapp.pojo.WagonType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 23.08.16.
 */
public class Document {
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sys_date")
    @Expose
    private int sysDate;
    @SerializedName("sys_number")
    @Expose
    private String sysNumber;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("reserve")
    @Expose
    private String reserve;
    @SerializedName("serial_blank")
    @Expose
    private String serialBlank;
    @SerializedName("number_blank")
    @Expose
    private String numberBlank;
    @SerializedName("security_cipher")
    @Expose
    private String securityCipher;
    @SerializedName("security_code")
    @Expose
    private String securityCode;
    @SerializedName("security_charline")
    @Expose
    private String securityCharline;
    @SerializedName("kind")
    @Expose
    private TicketKind kind;
    @SerializedName("childres")
    @Expose
    private String childres;
    @SerializedName("departure_date")
    @Expose
    private long departureDate;
    @SerializedName("arrival_date")
    @Expose
    private long arrivalDate;
    @SerializedName("station_from_code")
    @Expose
    private int stationFromCode;
    @SerializedName("station_from_name")
    @Expose
    private String stationFromName;
    @SerializedName("station_to_code")
    @Expose
    private int stationToCode;
    @SerializedName("station_to_name")
    @Expose
    private String stationToName;
    @SerializedName("train")
    @Expose
    private String train;
    @SerializedName("train_fasted")
    @Expose
    private int trainFasted;
    @SerializedName("train_class")
    @Expose
    private int trainClass;
    @SerializedName("train_model")
    @Expose
    private int trainModel;
    @SerializedName("train_category")
    @Expose
    private int trainCategory;
    @SerializedName("train_arrival")
    @Expose
    private String trainArrival;
    @SerializedName("train_departure")
    @Expose
    private String trainDeparture;
    @SerializedName("wagon")
    @Expose
    private int wagon;
    @SerializedName("wagon_type")
    @Expose
    private WagonType wagonType;
    @SerializedName("wagon_class")
    @Expose
    private WagonClass wagonClass;
    @SerializedName("wagon_layout")
    @Expose
    private String wagonLayout;
    @SerializedName("wagon_firm")
    @Expose
    private boolean wagonFirm;
    @SerializedName("railway_code")
    @Expose
    private String railwayCode;
    @SerializedName("railway")
    @Expose
    private String railway;
    @SerializedName("places_count")
    @Expose
    private int placesCount;
    @SerializedName("places")
    @Expose
    private List<Integer> places = new ArrayList<Integer>();
    @SerializedName("services")
    @Expose
    private List<TicketService> services = new ArrayList<TicketService>();
    @SerializedName("children_category")
    @Expose
    private String childrenCategory;
    @SerializedName("children_birthday")
    @Expose
    private int childrenBirthday;
    @SerializedName("transportation_kind")
    @Expose
    private String transportationKind;
    @SerializedName("transportation_weight")
    @Expose
    private String transportationWeight;
    @SerializedName("transportation_date")
    @Expose
    private int transportationDate;
    @SerializedName("transportation_uid")
    @Expose
    private String transportationUid;
    @SerializedName("transportation_sys_number")
    @Expose
    private String transportationSysNumber;
    @SerializedName("reserve_date")
    @Expose
    private int reserveDate;
    @SerializedName("reserve_cost")
    @Expose
    private double reserveCost;
    @SerializedName("return_uid")
    @Expose
    private String returnUid;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("middlename")
    @Expose
    private String middlename;
    @SerializedName("privilege_category")
    @Expose
    private String privilegeCategory;
    @SerializedName("privilege_country")
    @Expose
    private String privilegeCountry;
    @SerializedName("privilege_serial")
    @Expose
    private String privilegeSerial;
    @SerializedName("privilege_military")
    @Expose
    private String privilegeMilitary;
    @SerializedName("privilege_number")
    @Expose
    private String privilegeNumber;
    @SerializedName("bonus_card_number")
    @Expose
    private String bonusCardNumber;
    @SerializedName("bonus_type")
    @Expose
    private String bonusType;
    @SerializedName("bonus_cost")
    @Expose
    private String bonusCost;
    @SerializedName("coupons")
    @Expose
    private List<Coupon> coupons = new ArrayList<Coupon>();
    @SerializedName("transaction")
    @Expose
    private String transaction;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("transaction_terminal")
    @Expose
    private String transactionTerminal;
    @SerializedName("transaction_sys_terminal")
    @Expose
    private String transactionSysTerminal;
    @SerializedName("costs")
    @Expose
    private List<TicketCost> costs = new ArrayList<TicketCost>();
    @SerializedName("withholding")
    @Expose
    private List<Withholding> withholding = new ArrayList<Withholding>();
    @SerializedName("insurance_company_address")
    @Expose
    private String insuranceCompanyAddress;
    @SerializedName("insurance_company_name")
    @Expose
    private String insuranceCompanyName;
    @SerializedName("insurance_company_telephone")
    @Expose
    private String insuranceCompanyTelephone;
    @SerializedName("text")
    @Expose
    private String text;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSysDate() {
        return sysDate;
    }

    public void setSysDate(int sysDate) {
        this.sysDate = sysDate;
    }

    public String getSysNumber() {
        return sysNumber;
    }

    public void setSysNumber(String sysNumber) {
        this.sysNumber = sysNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getSerialBlank() {
        return serialBlank;
    }

    public void setSerialBlank(String serialBlank) {
        this.serialBlank = serialBlank;
    }

    public String getNumberBlank() {
        return numberBlank;
    }

    public void setNumberBlank(String numberBlank) {
        this.numberBlank = numberBlank;
    }

    public String getSecurityCipher() {
        return securityCipher;
    }

    public void setSecurityCipher(String securityCipher) {
        this.securityCipher = securityCipher;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getSecurityCharline() {
        return securityCharline;
    }

    public void setSecurityCharline(String securityCharline) {
        this.securityCharline = securityCharline;
    }

    public TicketKind getKind() {
        return kind;
    }

    public void setKind(TicketKind kind) {
        this.kind = kind;
    }

    public String getChildres() {
        return childres;
    }

    public void setChildres(String childres) {
        this.childres = childres;
    }

    public long getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(long departureDate) {
        this.departureDate = departureDate;
    }

    public long getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(long arrivalDate) {
        this.arrivalDate = arrivalDate;
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

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public int getTrainFasted() {
        return trainFasted;
    }

    public void setTrainFasted(int trainFasted) {
        this.trainFasted = trainFasted;
    }

    public int getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(int trainClass) {
        this.trainClass = trainClass;
    }

    public int getTrainModel() {
        return trainModel;
    }

    public void setTrainModel(int trainModel) {
        this.trainModel = trainModel;
    }

    public int getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(int trainCategory) {
        this.trainCategory = trainCategory;
    }

    public String getTrainArrival() {
        return trainArrival;
    }

    public void setTrainArrival(String trainArrival) {
        this.trainArrival = trainArrival;
    }

    public String getTrainDeparture() {
        return trainDeparture;
    }

    public void setTrainDeparture(String trainDeparture) {
        this.trainDeparture = trainDeparture;
    }

    public int getWagon() {
        return wagon;
    }

    public void setWagon(int wagon) {
        this.wagon = wagon;
    }

    public WagonType getWagonType() {
        return wagonType;
    }

    public void setWagonType(WagonType wagonType) {
        this.wagonType = wagonType;
    }

    public WagonClass getWagonClass() {
        return wagonClass;
    }

    public void setWagonClass(WagonClass wagonClass) {
        this.wagonClass = wagonClass;
    }

    public String getWagonLayout() {
        return wagonLayout;
    }

    public void setWagonLayout(String wagonLayout) {
        this.wagonLayout = wagonLayout;
    }

    public boolean isWagonFirm() {
        return wagonFirm;
    }

    public void setWagonFirm(boolean wagonFirm) {
        this.wagonFirm = wagonFirm;
    }

    public String getRailwayCode() {
        return railwayCode;
    }

    public void setRailwayCode(String railwayCode) {
        this.railwayCode = railwayCode;
    }

    public String getRailway() {
        return railway;
    }

    public void setRailway(String railway) {
        this.railway = railway;
    }

    public int getPlacesCount() {
        return placesCount;
    }

    public void setPlacesCount(int placesCount) {
        this.placesCount = placesCount;
    }

    public List<Integer> getPlaces() {
        return places;
    }

    public void setPlaces(List<Integer> places) {
        this.places = places;
    }

    public List<TicketService> getServices() {
        return services;
    }

    public void setServices(List<TicketService> services) {
        this.services = services;
    }

    public String getChildrenCategory() {
        return childrenCategory;
    }

    public void setChildrenCategory(String childrenCategory) {
        this.childrenCategory = childrenCategory;
    }

    public int getChildrenBirthday() {
        return childrenBirthday;
    }

    public void setChildrenBirthday(int childrenBirthday) {
        this.childrenBirthday = childrenBirthday;
    }

    public String getTransportationKind() {
        return transportationKind;
    }

    public void setTransportationKind(String transportationKind) {
        this.transportationKind = transportationKind;
    }

    public String getTransportationWeight() {
        return transportationWeight;
    }

    public void setTransportationWeight(String transportationWeight) {
        this.transportationWeight = transportationWeight;
    }

    public int getTransportationDate() {
        return transportationDate;
    }

    public void setTransportationDate(int transportationDate) {
        this.transportationDate = transportationDate;
    }

    public String getTransportationUid() {
        return transportationUid;
    }

    public void setTransportationUid(String transportationUid) {
        this.transportationUid = transportationUid;
    }

    public String getTransportationSysNumber() {
        return transportationSysNumber;
    }

    public void setTransportationSysNumber(String transportationSysNumber) {
        this.transportationSysNumber = transportationSysNumber;
    }

    public int getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(int reserveDate) {
        this.reserveDate = reserveDate;
    }

    public double getReserveCost() {
        return reserveCost;
    }

    public void setReserveCost(double reserveCost) {
        this.reserveCost = reserveCost;
    }

    public String getReturnUid() {
        return returnUid;
    }

    public void setReturnUid(String returnUid) {
        this.returnUid = returnUid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getPrivilegeCategory() {
        return privilegeCategory;
    }

    public void setPrivilegeCategory(String privilegeCategory) {
        this.privilegeCategory = privilegeCategory;
    }

    public String getPrivilegeCountry() {
        return privilegeCountry;
    }

    public void setPrivilegeCountry(String privilegeCountry) {
        this.privilegeCountry = privilegeCountry;
    }

    public String getPrivilegeSerial() {
        return privilegeSerial;
    }

    public void setPrivilegeSerial(String privilegeSerial) {
        this.privilegeSerial = privilegeSerial;
    }

    public String getPrivilegeMilitary() {
        return privilegeMilitary;
    }

    public void setPrivilegeMilitary(String privilegeMilitary) {
        this.privilegeMilitary = privilegeMilitary;
    }

    public String getPrivilegeNumber() {
        return privilegeNumber;
    }

    public void setPrivilegeNumber(String privilegeNumber) {
        this.privilegeNumber = privilegeNumber;
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

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionTerminal() {
        return transactionTerminal;
    }

    public void setTransactionTerminal(String transactionTerminal) {
        this.transactionTerminal = transactionTerminal;
    }

    public String getTransactionSysTerminal() {
        return transactionSysTerminal;
    }

    public void setTransactionSysTerminal(String transactionSysTerminal) {
        this.transactionSysTerminal = transactionSysTerminal;
    }

    public List<TicketCost> getCosts() {
        return costs;
    }

    public void setCosts(List<TicketCost> costs) {
        this.costs = costs;
    }

    public List<Withholding> getWithholding() {
        return withholding;
    }

    public void setWithholding(List<Withholding> withholding) {
        this.withholding = withholding;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
