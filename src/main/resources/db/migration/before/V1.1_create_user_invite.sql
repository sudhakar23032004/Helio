create table helio.user_invite
(   user_id uniqueidentifier not null primary key,
    email_id          varchar(max),
    mobile_number     varchar(10),
    twilio_message_id varchar(50),
    email_message_id  varchar(50),
    otp               bigint,
    status            varchar(10),
    created_by        varchar(max),
    created_timestamp datetime,
    updated_by        varchar(max),
    updated_timestamp datetime )
GO