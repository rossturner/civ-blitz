create table player_dlc_setting
(
    player_id varchar(30),
    dlc_name  varchar(60),
    primary key (player_id, dlc_name)
);
