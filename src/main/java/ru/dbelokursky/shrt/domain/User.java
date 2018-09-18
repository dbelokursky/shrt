package ru.dbelokursky.shrt.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "shrt_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private Boolean enabled;
}
