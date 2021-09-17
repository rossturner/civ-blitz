alter table match_signup add column final_points_awarded double precision;
alter table player add column average_points_earned double precision default 0;