create table scored_objective (
    match_id integer,
    player_id varchar(30),
    objective varchar(60),
    primary key (match_id, player_id, objective)
);