package com.egnese.eggwallet.dto;

/**
 * Created by adityaagrawal on 25/11/15.
 */
public class ErrorDTO {
    private String name;
    private String status;
    private Integer statusCode;
    private String details;
    private String message;


    @Override
    public String toString() {
        return "ErrorDTO{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", statusCode=" + statusCode +
                ", details='" + details + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
