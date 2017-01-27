package com.uzapp.pojo.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 12.08.16.
 */
public class CreateAccountInfo {
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String password;
    @SerializedName("student_id")
    private String studentId;
    @SerializedName("fb_id")
    private String fbId;
    @SerializedName("vk_id")
    private String vkId;
    @SerializedName("ok_id")
    private String okId;

    public CreateAccountInfo(CreateAccountInfoBuilder builder) {
        this.deviceId = builder.deviceId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.password = builder.password;
        this.studentId = builder.studentId;
        this.fbId = builder.fbId;
        this.vkId = builder.vkId;
        this.okId = builder.okId;
    }


    public static class CreateAccountInfoBuilder {
        private String deviceId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String password;
        private String studentId;
        private String fbId, vkId, okId;

        public CreateAccountInfoBuilder(String deviceId, String email) {
            this.deviceId = deviceId;
            this.email = email;
        }

        public CreateAccountInfoBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CreateAccountInfoBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CreateAccountInfoBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CreateAccountInfoBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public CreateAccountInfoBuilder setStudentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public CreateAccountInfoBuilder setFbId(String fbId) {
            this.fbId = fbId;
            return this;
        }

        public CreateAccountInfoBuilder setVkId(String vkId) {
            this.vkId = vkId;
            return this;
        }

        public CreateAccountInfoBuilder setOkId(String okId) {
            this.okId = okId;
            return this;
        }

        public CreateAccountInfo build(){
            return new CreateAccountInfo(this);
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFbId() {
        return fbId;
    }

    public String getVkId() {
        return vkId;
    }

    public String getOkId() {
        return okId;
    }
}
