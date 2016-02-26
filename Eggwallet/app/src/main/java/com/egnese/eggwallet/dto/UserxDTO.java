package com.egnese.eggwallet.dto;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public class UserxDTO {
    private String avatarCode;
    private String name;
    private String mobile;
    private String email;
    private String id;
    private String city;
    private String password;
    private String referralCode;
    private String created;
    private String accessToken;
    private String dob;
    private String gender;
    private String lastUpdated;


    @Override
    public String toString() {
        return "UserxDTO{" +
                "avatarCode='" + avatarCode + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", referralCode='" + referralCode + '\'' +
                ", created='" + created + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarCode() {
        return avatarCode;
    }

    public void setAvatarCode(String avatarCode) {
        this.avatarCode = avatarCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
