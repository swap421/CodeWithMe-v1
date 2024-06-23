package com.connectify.connectify.Entity;

import com.connectify.connectify.Model.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserDao {
    @Id
    private String gmailId;
    private String username;
    private Status status;

}
