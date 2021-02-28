package hello.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

public class User {
    private Integer id;
    private String username;
    private String avatar;
    @JsonIgnore
    private String bcryptedPassword;
    private Instant createdAt;
    private Instant updatedAt;

    public String getAvatar() {
        return avatar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public User(Integer id, String name, String bcryptedPassword) {
        this.id = id;
        this.username = name;
        this.avatar = "";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.bcryptedPassword = bcryptedPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBcryptedPassword() {
        return bcryptedPassword;
    }

    public void setBcryptedPassword(String bcryptedPassword) {
        this.bcryptedPassword = bcryptedPassword;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
