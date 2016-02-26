package com.egnese.eggwallet.dto;

import java.util.Date;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public class SharedWalletDTO {
    private String EggUserId;
    private Date date;
    private Boolean isReliable;
    private Boolean isUsable;

    public String getEggUserId() {
        return EggUserId;
    }

    public void setEggUserId(String eggUserId) {
        EggUserId = eggUserId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getIsReliable() {
        return isReliable;
    }

    public void setIsReliable(Boolean isReliable) {
        this.isReliable = isReliable;
    }

    public Boolean getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(Boolean isUsable) {
        this.isUsable = isUsable;
    }
}
