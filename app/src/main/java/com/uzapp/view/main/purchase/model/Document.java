package com.uzapp.view.main.purchase.model;

/**
 * Created by Vladimir on 01.09.2016.
 */
public class Document {

    private int number;
    private String kind;
    private String privilege;
    private int count_place;
    private String firstname;
    private String lastname;
    private String middlename;
    private int birthday;
    private String passport;

    public Document() {}

    private Document(DocumentBuilder builder) {
        this.number = builder.number;
        this.kind = builder.kind;
        this.privilege = builder.privilege;
        this.count_place = builder.count_place;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.middlename = builder.middlename;
        this.birthday = builder.birthday;
        this.passport = builder.passport;
    }

    public int getNumber() {
        return number;
    }

    public String getKind() {
        return kind;
    }

    public String getPrivilege() {
        return privilege;
    }

    public int getCountPlace() {
        return count_place;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getMiddleName() {
        return middlename;
    }

    public int getBirthday() {
        return birthday;
    }

    public String getPassport() {
        return passport;
    }

    //Builder Class
    public static class DocumentBuilder {

        private int number;
        private String kind;
        private String privilege;
        private int count_place;
        private String firstname;
        private String lastname;
        private String middlename;
        private int birthday;
        private String passport;

        public DocumentBuilder() {}

        public DocumentBuilder(int number, String kind, String privilege, int count_place, String firstname,
                               String lastname, String middlename, int birthday, String passport) {
            this.number = number;
            this.kind = kind;
            this.privilege = privilege;
            this.count_place = count_place;
            this.firstname = firstname;
            this.lastname = lastname;
            this.middlename = middlename;
            this.birthday = birthday;
            this.passport = passport;
        }

        public DocumentBuilder setNumber(int number) {
            this.number = number;
            return this;
        }

        public DocumentBuilder setKind(String kind) {
            this.kind = kind;
            return this;
        }

        public DocumentBuilder setPrivilege(String privilege) {
            this.privilege = privilege;
            return this;
        }

        public DocumentBuilder setCountPlace(int countPlace) {
            this.count_place = countPlace;
            return this;
        }

        public DocumentBuilder setFirstName(String firstName) {
            this.firstname = firstName;
            return this;
        }

        public DocumentBuilder setLastName(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public DocumentBuilder setMiddleName(String middlename) {
            this.middlename = middlename;
            return this;
        }

        public DocumentBuilder setBirthday(int birthday) {
            this.birthday = birthday;
            return this;
        }

        public DocumentBuilder setPassport(String passport) {
            this.passport = passport;
            return this;
        }

        public Document build(){
            return new Document(this);
        }

    }
}
