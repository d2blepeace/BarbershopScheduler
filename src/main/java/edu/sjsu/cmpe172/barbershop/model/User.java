package edu.sjsu.cmpe172.barbershop.model;

import java.time.LocalDateTime;

/**
 * Represent a general user of the system
 * A user can have different role: Customer, Provider (Barber/Nail technician), Admin
 */
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    public User() {}
    
    public User(Long id, String username, String passwordHash, 
        String email, String role, LocalDateTime createdAt) {
            this.id = id;
            this.username = username;
            this.passwordHash = passwordHash;
            this.email = email;
            this.role = role;
            this.createdAt = createdAt;
    }

    //Getters and setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPasswordHash() {return passwordHash;}
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
