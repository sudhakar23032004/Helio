package com.helio.data.enums;

public enum UserConnection {

    PENDING,
    ACCEPTED,
    WITHDRAWN;

    public static String name(String status){
        return UserConnection.valueOf(status).name();
    }
}
