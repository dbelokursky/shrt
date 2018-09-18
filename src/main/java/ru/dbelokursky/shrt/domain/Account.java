package ru.dbelokursky.shrt.domain;

import lombok.Data;

@Data
public class Account {

    boolean success;

    String description;

    String password;
}
