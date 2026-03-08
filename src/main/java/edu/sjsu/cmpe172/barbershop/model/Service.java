package edu.sjsu.cmpe172.barbershop.model;

public class Service {
    private Long serviceId;
    private String serviceName;
    private int duration;       // in minutes
    private double price;
    private String type;

    public Service() {}

    public Service(Long serviceId, String serviceName, int duration, double price, String type) {
            this.serviceId = serviceId;
            this.serviceName = serviceName;
            this.duration = duration;
            this.price = price;
            this.type = type;
    }

    // Getters and Settters
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
}
