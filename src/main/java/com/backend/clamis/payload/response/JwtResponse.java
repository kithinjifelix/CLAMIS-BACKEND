package com.backend.clamis.payload.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String name;
    private List<String> roles;
    private User user;

    public JwtResponse(String accessToken, Long id, String username, String email, String name, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.name = name;
        this.user = new User();
        this.user.id = id;
        this.user.username = username;
        this.user.email = email;
        this.user.name = name;
        this.user.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; };

    public List<String> getRoles() {
        return roles;
    }
    public User getUser() { return user; }
}

class User {
    public Long id;
    public String username;
    public String email;
    public String name;
    public List<String> roles;

    public List<String> getRoles() {
        return roles;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; };
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
