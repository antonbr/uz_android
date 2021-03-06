package com.uzapp.pojo;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by vika on 17.07.16.
 */
public class PopularStation extends RealmObject {
    long code;
    @Required
    String name;
    String railway;
    Date accessTime;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRailway() {
        return railway;
    }

    public void setRailway(String railway) {
        this.railway = railway;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public void setValues(long code, String name, String railway, Date accessTime){
        this.code =  code;
        this.name = name;
        this.railway = railway;
        this.accessTime = accessTime;
    }
}
