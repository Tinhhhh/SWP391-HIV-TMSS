package com.swp391.hivtmss.model.payload.enums;

import lombok.Getter;

@Getter
public enum RedisPrefix {
    TOKEN_BLACKLIST("TOKEN_BLACKLIST"),
    TOKEN_IAT_AVAILABLE("TOKEN_IAT_AVAILABLE"),
    TOKEN_RESET_PASSWORD("TOKEN_RESET_PASSWORD");

    private final String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }

}
