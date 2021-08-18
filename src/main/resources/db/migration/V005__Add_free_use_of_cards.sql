alter table match_signup add column civ_ability_is_free boolean default false;
alter table match_signup add column leader_ability_is_free boolean default false;
alter table match_signup add column unique_infrastructure_is_free boolean default false;
alter table match_signup add column unique_unit_is_free boolean default false;