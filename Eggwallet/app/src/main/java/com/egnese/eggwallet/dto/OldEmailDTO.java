package com.egnese.eggwallet.dto;

import java.util.Date;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public class OldEmailDTO {
    private String email;
    private Date added;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
