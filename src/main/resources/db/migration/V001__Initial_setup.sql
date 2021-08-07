create table player (
    player_id          varchar(30) primary key,
    discord_username    varchar(30),
    balance             double precision,
    total_points_earned double precision,
    ranking_score       double precision
);

create table collection (
    player_id          varchar(30),
    card_trait_type     varchar(60),
    quantity            integer,
    primary key (player_id, card_trait_type)
);

create table match (
    match_id            SERIAL PRIMARY KEY,
    match_name          varchar(200),
    timeslot            varchar(100),
    match_state         varchar(30)
);

create table match_signup (
   match_id                     integer,
   player_id                    varchar(30),
   card_civ_ability             varchar(60),
   card_leader_ability          varchar(60),
   card_unique_infrastruture    varchar(60),
   card_unique_unit             varchar(60),
   start_bias_civ_type          varchar(60),
   committed                    boolean,
   card_booster_claimed         boolean,
   awarded_score                double precision,
   primary key (match_id, player_id)
);



-- CREATE INDEX idx_contract_symbol on contract (symbol);