create table player_collection
(
    player_id       varchar(30),
    card_identifier varchar(80),
    quantity        integer,
    primary key (player_id, card_identifier)
);
insert into player_collection (player_id, card_identifier, quantity)
select player_id, concat('COMMON_', card_trait_type) as card_identifier, quantity from collection;
drop table collection;