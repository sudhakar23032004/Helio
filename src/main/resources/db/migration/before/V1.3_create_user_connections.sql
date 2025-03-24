create table helio.user_connection
(   connection_id uniqueidentifier not null primary key,
    from_user_id      uniqueidentifier,
    to_user_id        uniqueidentifier,
    message           varchar(max),
    status            varchar(10),
    created_by        varchar(max),
    created_timestamp datetime,
    updated_by        varchar(max),
    updated_timestamp datetime,
    FOREIGN KEY (from_user_id) REFERENCES helio.users(user_id),
    FOREIGN KEY (to_user_id) REFERENCES helio.users(user_id))
GO