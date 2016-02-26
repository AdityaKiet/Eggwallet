package com.egnese.eggwallet.dto;

/**
 * Created by adityaagrawal on 23/11/15.
 */
public class SessionDTO {
    private String gcmID;
    private UserxDTO userxDTO;

    public UserxDTO getUserxDTO() {
        return userxDTO;
    }

    public void setUserxDTO(UserxDTO userxDTO) {
        this.userxDTO = userxDTO;
    }

    public String getGcmID() {
        return gcmID;
    }

    public void setGcmID(String gcmID) {
        this.gcmID = gcmID;
    }
}
