package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.UserRealmProxy;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by vika on 17.08.16.
 */
@Parcel(implementations = {UserRealmProxy.class },
        value = Parcel.Serialization.BEAN,
        analyze = { User.class })
public class User extends RealmObject{
    @PrimaryKey
    private long id;
    @Required
    @SerializedName("first_name")
    private String firstName;
    @Required
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("middle_name")
    private String middleName;
    @Required
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("is_buy_by_default")
    private boolean isBuyByDefault;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isBuyByDefault() {
        return isBuyByDefault;
    }

    public void setBuyByDefault(boolean buyByDefault) {
        isBuyByDefault = buyByDefault;
    }
}
