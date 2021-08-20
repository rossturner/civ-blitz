alter table player drop column boosters_available;

create table card_pack (
    pack_id             SERIAL PRIMARY KEY,
    player_id           varchar(30),
    pack_type           varchar(60),
    num_civ_ability     integer,
    num_leader_ability  integer,
    num_unique_infrastructure   integer,
    num_unique_unit             integer
);