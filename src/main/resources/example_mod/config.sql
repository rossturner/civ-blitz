INSERT OR REPLACE INTO Players
(Domain,
 CivilizationType,
 Portrait,
 PortraitBackground,
 LeaderType,
 LeaderName,
 LeaderIcon,
 LeaderAbilityName,
 LeaderAbilityDescription,
 LeaderAbilityIcon,
 CivilizationName,
 CivilizationIcon,
 CivilizationAbilityName,
 CivilizationAbilityDescription,
 CivilizationAbilityIcon)
VALUES
('Players:Expansion2_Players',
 'CIVILIZATION_IMP_JAPABULLARABCREE',
--  (SELECT Players.Portrait FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.LeaderType = 'LEADER_T_ROOSEVELT'),
 'LEADER_T_ROOSEVELT_NEUTRAL',
--  (SELECT Players.PortraitBackground FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.LeaderType = 'LEADER_T_ROOSEVELT'),
 'LEADER_T_ROOSEVELT_BACKGROUND',
 'LEADER_IMP_JAPABULLARABCREE',
 'LOC_LEADER_IMP_JAPABULLARABCREE',
 (SELECT Players.LeaderIcon FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.LeaderType = 'LEADER_T_ROOSEVELT'),
 (SELECT Players.LeaderAbilityName FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.LeaderType = 'LEADER_T_ROOSEVELT'),
 (SELECT Players.LeaderAbilityDescription FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.LeaderType = 'LEADER_T_ROOSEVELT'),
 (SELECT Players.LeaderAbilityIcon FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.LeaderType = 'LEADER_T_ROOSEVELT'),
 (SELECT Players.CivilizationName FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.CivilizationType = 'CIVILIZATION_JAPAN' LIMIT 1),
 (SELECT Players.CivilizationIcon FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.CivilizationType = 'CIVILIZATION_JAPAN' LIMIT 1),
 (SELECT Players.CivilizationAbilityName FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.CivilizationType = 'CIVILIZATION_JAPAN' LIMIT 1),
 (SELECT Players.CivilizationAbilityDescription FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.CivilizationType = 'CIVILIZATION_JAPAN' LIMIT 1),
 (SELECT Players.CivilizationAbilityIcon FROM Players WHERE Players.Domain = 'Players:Expansion2_Players' and Players.CivilizationType = 'CIVILIZATION_JAPAN' LIMIT 1)
);



-- These are the other traits (not CA or LA) including any granted by the CA or LA
-- use subtype from Imperium cards
INSERT OR REPLACE INTO PlayerItems (Domain, CivilizationType, LeaderType, Type, Icon, Name, Description, SortIndex)
SELECT 'Players:Expansion2_Players', 'CIVILIZATION_IMP_JAPABULLARABCREE', 'LEADER_IMP_JAPABULLARABCREE', PlayerItems.Type, PlayerItems.Icon, PlayerItems.Name, PlayerItems.Description, 10
FROM PlayerItems
WHERE Domain = 'Players:Expansion2_Players' and CivilizationType = 'CIVILIZATION_ARABIA' and Type = 'BUILDING_MADRASA';

INSERT OR REPLACE INTO PlayerItems (Domain, CivilizationType, LeaderType, Type, Icon, Name, Description, SortIndex)
SELECT 'Players:Expansion2_Players', 'CIVILIZATION_IMP_JAPABULLARABCREE', 'LEADER_IMP_JAPABULLARABCREE', PlayerItems.Type, PlayerItems.Icon, PlayerItems.Name, PlayerItems.Description, 20
FROM PlayerItems
WHERE Domain = 'Players:Expansion2_Players' and CivilizationType = 'CIVILIZATION_CREE' and Type = 'UNIT_CREE_OKIHTCITAW';