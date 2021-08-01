
INSERT INTO Types
(Type, Kind)
VALUES	('LEADER_IMP_JAPABULLARABCREE', 'KIND_LEADER');

INSERT INTO Leaders
(LeaderType, Name, InheritFrom, SceneLayers, Sex, SameSexPercentage)
SELECT 'LEADER_IMP_JAPABULLARABCREE', 'LOC_LEADER_IMP_JAPABULLARABCREE', Leaders.InheritFrom , Leaders.SceneLayers , Leaders.Sex, Leaders.SameSexPercentage
FROM Leaders
WHERE Leaders.LeaderType = 'LEADER_T_ROOSEVELT';

--------------------------------------------------------------------------------------------------------------------------
-- DiplomacyInfo
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO DiplomacyInfo (Type, BackgroundImage)
SELECT 'LEADER_IMP_JAPABULLARABCREE', DiplomacyInfo.BackgroundImage
FROM DiplomacyInfo
WHERE DiplomacyInfo.Type = 'LEADER_T_ROOSEVELT';
--------------------------------------------------------------------------------------------------------------------------
-- LeaderTraits
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO LeaderTraits (LeaderType, TraitType)
VALUES ('LEADER_IMP_JAPABULLARABCREE', 'TRAIT_LEADER_ANTIQUES_AND_PARKS');
--------------------------------------------------------------------------------------------------------------------------
-- LeaderQuotes
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO LeaderQuotes (LeaderType, Quote)
SELECT 'LEADER_IMP_JAPABULLARABCREE', Quote
FROM LeaderQuotes
WHERE LeaderType = 'LEADER_T_ROOSEVELT';
--------------------------------------------------------------------------------------------------------------------------
-- HistoricalAgendas
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO HistoricalAgendas (LeaderType, AgendaType)
SELECT 'LEADER_IMP_JAPABULLARABCREE', AgendaType
FROM HistoricalAgendas
WHERE LeaderType = 'LEADER_T_ROOSEVELT';
--------------------------------------------------------------------------------------------------------------------------
-- AgendaPreferredLeaders
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO AgendaPreferredLeaders (LeaderType, AgendaType)
SELECT 'LEADER_IMP_JAPABULLARABCREE', AgendaType
FROM AgendaPreferredLeaders
WHERE LeaderType = 'LEADER_T_ROOSEVELT';
--------------------------------------------------------------------------------------------------------------------------
-- FavoredReligions
--------------------------------------------------------------------------------------------------------------------------
INSERT OR REPLACE INTO FavoredReligions (LeaderType, ReligionType)
SELECT 'LEADER_IMP_JAPABULLARABCREE', ReligionType
FROM FavoredReligions
WHERE LeaderType = 'LEADER_T_ROOSEVELT';
--==========================================================================================================================
-- LEADERS: LOADING INFO
--==========================================================================================================================
INSERT INTO LoadingInfo (LeaderType, BackgroundImage, ForegroundImage, PlayDawnOfManAudio)
SELECT 'LEADER_IMP_JAPABULLARABCREE', BackgroundImage, ForegroundImage, PlayDawnOfManAudio
FROM LoadingInfo
WHERE LeaderType = 'LEADER_T_ROOSEVELT';