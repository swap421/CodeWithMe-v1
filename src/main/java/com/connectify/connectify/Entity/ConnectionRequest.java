package com.connectify.connectify.Entity;

import jakarta.persistence.*;

@Entity
public class ConnectionRequest {

    public ConnectionRequest() {
    }

    public ConnectionRequest(User fromUser, long toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_id")
    private User fromUser;
    private Long toUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public Long getToUser() {
        return toUser;
    }

    public void setToUser(long toUser) {
        this.toUser = toUser;
    }
}
