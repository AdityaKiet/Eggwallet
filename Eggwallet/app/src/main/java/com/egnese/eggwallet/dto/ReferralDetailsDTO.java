package com.egnese.eggwallet.dto;

import java.util.Date;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public class ReferralDetailsDTO {
    private String refferedTo;
    private Date referralDate;

    public String getRefferedTo() {
        return refferedTo;
    }

    public void setRefferedTo(String refferedTo) {
        this.refferedTo = refferedTo;
    }

    public Date getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(Date referralDate) {
        this.referralDate = referralDate;
    }
}
