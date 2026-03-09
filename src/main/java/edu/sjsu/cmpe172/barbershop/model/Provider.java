package edu.sjsu.cmpe172.barbershop.model;

/**
 * Represent a service provider:
 * - Barber
 * - Nail Technician
 * 
 * A provider will offer service and availability time slots
 */
public class Provider {
    private Long providerId;
    private String name;
    private String bio;
    private String avatarUrl;           //tends to change in future 
    private boolean isActive;

    public Provider() {}

    public Provider(Long providerId, String name, String bio, String avatarUrl, boolean isActive) {
        this.providerId = providerId;
        this.name = name;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.isActive = isActive;
    }

    //Getters and Setters
    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
