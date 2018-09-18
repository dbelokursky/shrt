package ru.dbelokursky.shrt.domain;

import lombok.Data;

@Data
public class Account {

    private boolean success;

    private String description;

    private String password;
}
