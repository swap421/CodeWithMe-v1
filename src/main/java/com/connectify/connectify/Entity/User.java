package com.connectify.connectify.Entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Getter
    private String gmailId;
    @Getter
    private String email;
    @Getter
    private String username;
    @Getter
    private String leetcodeUsername;
    @Getter
    private String codeforcesUsername;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String profileImage;
    @Getter
    private String bio;
    @Getter
    private Date createdAt;
    @Getter
    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
    private List<ConnectionRequest> receivedConnectionRequests; // Requests received by the user
    @Getter
    @OneToMany(mappedBy = "activeUser", cascade = CascadeType.ALL)
    private List<ActiveConnection> activeConnections;

    @Getter
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Rating rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGmailId(String gmailId) {
        this.gmailId = gmailId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLeetcodeUsername(String leetcodeUsername) {
        this.leetcodeUsername = leetcodeUsername;
    }

    public void setCodeforcesUsername(String codeforcesUsername) {
        this.codeforcesUsername = codeforcesUsername;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setReceivedConnectionRequests(List<ConnectionRequest> receivedConnectionRequests) {
        this.receivedConnectionRequests = receivedConnectionRequests;
    }

    public void setActiveConnections(List<ActiveConnection> activeConnections) {
        this.activeConnections = activeConnections;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", gmailId='" + gmailId + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", leetcodeUsername='" + leetcodeUsername + '\'' +
                ", codeforcesUsername='" + codeforcesUsername + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", bio='" + bio + '\'' +
                ", createdAt=" + createdAt +
                ", receivedConnectionRequests=" + receivedConnectionRequests +
                ", activeConnections=" + activeConnections +
                '}';
    }
}

