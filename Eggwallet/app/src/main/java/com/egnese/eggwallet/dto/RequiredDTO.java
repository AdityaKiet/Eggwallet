package com.egnese.eggwallet.dto;

/**
 * Created by adityaagrawal on 25/12/15.
 */
public class RequiredDTO {
    private String id;
    private String accessToken;
    private String realm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
