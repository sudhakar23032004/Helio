package com.helio.data.enums;

public enum UserInviteStatus {
    INVITED,
    VALIDATED,
    ACTIVATED,
    DECLINED;

    public static String name(String status){
        return UserInviteStatus.valueOf(status).name();
    }
}
