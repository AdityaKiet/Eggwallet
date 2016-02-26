package com.egnese.eggwallet.dto;

import java.util.Date;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public class OldMobileDTO {
    private String mobile;
    private Date added;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
