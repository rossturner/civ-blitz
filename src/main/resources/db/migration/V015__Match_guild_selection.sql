create table match_guild
(
    match_id integer,
    guild_id varchar(30),
    primary key (match_id, guild_id)
);