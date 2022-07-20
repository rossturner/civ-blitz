create table draft_match
(
    match_id                SERIAL PRIMARY KEY,
    match_name              varchar(200),
    match_state             varchar(30),
    creation_date           timestamp,
    owner_id                varchar(30),
    random_upgrades_enabled boolean,
    upgrades_per_category   integer,
    cards_per_main_category integer,
    power_card_pool         integer,
    power_cards_allowed     integer,
    primary key (match_id)
);

create table draft_collection
(
    player_id       varchar(30),
    card_identifier varchar(80),
    quantity        integer,
    primary key (player_id, card_identifier)
);

create table draft_match_signup
(
    match_id            integer,
    player_id           varchar(30),
    start_bias_civ_type varchar(60),
    committed           boolean,
    primary key (match_id, player_id)
);

create table draft_match_signup_card
(
    match_id        integer     not null,
    player_id       varchar(30) not null,
    card_identifier varchar(80) not null,
    primary key (match_id, player_id, card_identifier)
);


