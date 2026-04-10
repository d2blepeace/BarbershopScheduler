package edu.sjsu.cmpe172.barbershop.dto;

/**
 * Response returned by MockNotification Controller 
 */
public class NotificationResponse {
    private String result;      //Success or Failure
    private String message;

    public NotificationResponse() {}

    public NotificationResponse(String result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
