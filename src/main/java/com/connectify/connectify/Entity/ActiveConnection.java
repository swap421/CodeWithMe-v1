package com.connectify.connectify.Entity;

import jakarta.persistence.*;

@Entity
public class ActiveConnection {
    public ActiveConnection() {
    }

    public ActiveConnection(long userId, User activeUser) {
        this.userId = userId;
        this.activeUser = activeUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private long userId;

    @ManyToOne
    @JoinColumn(name = "active_user")
    private User activeUser;


}
