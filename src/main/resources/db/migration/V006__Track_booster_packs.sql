alter table match_signup drop column card_booster_claimed;
alter table player add column boosters_available integer default 0;