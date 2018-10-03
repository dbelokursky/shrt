package ru.dbelokursky.shrt.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {

    private boolean success;

    private String description;

    private String password;
}
