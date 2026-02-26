package com.spacedrop.user.event;

public class UserEvent {

    private String type;
    private Long userId;
    private String email;
    private String role;

    public UserEvent() {
    }

    public UserEvent(String type, Long userId, String email, String role) {
        this.type = type;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
