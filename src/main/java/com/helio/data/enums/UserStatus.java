package com.helio.data.enums;

public enum UserStatus {

    ACTIVE,
    INACTIVE;

    public static String name(String status){
        return UserStatus.valueOf(status).name();
    }
}
