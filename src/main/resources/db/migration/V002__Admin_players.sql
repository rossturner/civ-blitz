alter table player add column is_admin boolean default false;
update player set is_admin = true where player_id = '291857466491273218'; -- Zsinj player ID

alter table player add column discord_avatar varchar(100);
