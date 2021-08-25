create table public_objective (
    match_id                     integer,
    objective                    varchar(60),
    primary key (match_id, objective)
);