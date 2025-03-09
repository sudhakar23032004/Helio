create table helio.users
(   user_id uniqueidentifier not null primary key,
    password          nvarchar(max),
    email_id          varchar(max),
    mobile_number     bigint,
    status            varchar(10),
    created_by        varchar(max),
    created_timestamp datetime,
    updated_by        varchar(max),
    updated_timestamp datetime,
    FOREIGN KEY (user_id) REFERENCES helio.user_invite(user_id))
GO