package com.egnese.eggwallet.dto;

/**
 * Created by adityaagrawal on 24/11/15.
 */
public class ResponseDTO {
    private Integer statusCode;
    private String status;
    private String name;
    private String message;
    private String details;
    private String data;

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "statusCode=" + statusCode +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
