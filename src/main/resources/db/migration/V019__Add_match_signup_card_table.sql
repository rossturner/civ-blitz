create table match_signup_card
(
    match_id        integer,
    player_id       varchar(30),
    card_identifier varchar(80),
    is_free         boolean default false,
    primary key (match_id, player_id, card_identifier)
);

insert into match_signup_card (match_id, player_id, card_identifier, is_free)
select match_id, player_id, concat('COMMON_', card_civ_ability) as card_identifier, civ_ability_is_free as is_free
from match_signup where card_civ_ability is not null and card_civ_ability != '';
insert into match_signup_card (match_id, player_id, card_identifier, is_free)
select match_id, player_id, concat('COMMON_', card_leader_ability) as card_identifier, leader_ability_is_free as is_free
from match_signup where card_leader_ability is not null and card_leader_ability != '';
insert into match_signup_card (match_id, player_id, card_identifier, is_free)
select match_id, player_id, concat('COMMON_', card_unique_infrastruture) as card_identifier, unique_infrastructure_is_free as is_free
from match_signup where card_unique_infrastruture is not null and card_unique_infrastruture != '';
insert into match_signup_card (match_id, player_id, card_identifier, is_free)
select match_id, player_id, concat('COMMON_', card_unique_unit) as card_identifier, unique_unit_is_free as is_free
from match_signup where card_unique_unit is not null and card_unique_unit != '';

alter table match_signup drop column card_civ_ability;
alter table match_signup drop column card_leader_ability;
alter table match_signup drop column card_unique_infrastruture;
alter table match_signup drop column card_unique_unit;
alter table match_signup drop column civ_ability_is_free;
alter table match_signup drop column leader_ability_is_free;
alter table match_signup drop column unique_infrastructure_is_free;
alter table match_signup drop column unique_unit_is_free;