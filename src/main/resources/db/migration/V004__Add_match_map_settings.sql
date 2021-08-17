alter table match add column spectator boolean default false;
alter table match add column map_type varchar(50);
alter table match add column map_size varchar(20);
alter table match add column world_age varchar(20);
alter table match add column sea_level varchar(20);
alter table match add column temperature varchar(20);
alter table match add column rainfall varchar(20);
alter table match add column city_states integer;
alter table match add column disaster_intensity integer;
