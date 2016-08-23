package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;
import com.uzapp.R;

/**
 * Created by vika on 23.08.16.
 */
public enum TicketKind {
    @SerializedName("full")
    FULL(R.string.ticket_kind_full),
    @SerializedName("child")
    CHILD(R.string.ticket_kind_child),
    @SerializedName("privilege")
    PRIVILEGE(R.string.ticket_kind_privilege),
    @SerializedName("military")
    MILITARY(R.string.ticket_kind_military);
    private int stringRes;

    TicketKind(int stringRes) {
        this.stringRes = stringRes;
    }

    public int getStringRes() {
        return stringRes;
    }
}
