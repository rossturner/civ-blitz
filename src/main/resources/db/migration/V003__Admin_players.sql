create table audit_log (
    player_id          varchar(30),
    discord_username    varchar(30),
    datetime            timestamp,
    action              varchar(300),
    primary key (player_id, datetime)
);