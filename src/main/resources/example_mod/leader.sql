
-- TODO NOT USED YET, TRY TO REUSE EXISTING LEADER

INSERT INTO Types
(Type, Kind)
VALUES	('LEADER_IMP_JAPABULLARABCREE', 'KIND_LEADER');

INSERT INTO Leaders
(LeaderType, Name, InheritFrom, SceneLayers, Sex, SameSexPercentage)
SELECT 'LEADER_IMP_JAPABULLARABCREE', Leaders.Name , Leaders.InheritFrom , Leaders.SceneLayers , Leaders.Sex, Leaders.SameSexPercentage
FROM Leaders
WHERE Leaders.LeaderType = 'LEADER_T_ROOSEVELT';

--------------------------------------------------------------------------------------------------------------------------
-- DiplomacyInfo
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO DiplomacyInfo
(Type,					BackgroundImage)
VALUES	('LEADER_GA_DUARTE',	'LEADER_GA_DUARTE_1');
--------------------------------------------------------------------------------------------------------------------------
-- LeaderTraits
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO LeaderTraits
(LeaderType,			TraitType)
VALUES	('LEADER_GA_DUARTE',	'TRAIT_LEADER_GA_DUARTE_VISIONARY'),
          ('LEADER_GA_DUARTE',	'TRAIT_UNIT_GA_TRINITARIO');
--------------------------------------------------------------------------------------------------------------------------
-- LeaderQuotes
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO LeaderQuotes
(LeaderType,			Quote)
VALUES	('LEADER_GA_DUARTE',	'LOC_PEDIA_LEADERS_PAGE_LEADER_GA_DUARTE_QUOTE');
--------------------------------------------------------------------------------------------------------------------------
-- HistoricalAgendas
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO HistoricalAgendas
(LeaderType,			AgendaType)
VALUES	('LEADER_GA_DUARTE',	'AGENDA_CULTURED');
--------------------------------------------------------------------------------------------------------------------------
-- AgendaPreferredLeaders
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO AgendaPreferredLeaders
(LeaderType,			AgendaType)
VALUES	('LEADER_GA_DUARTE',	'AGENDA_CIVILIZED');
--------------------------------------------------------------------------------------------------------------------------
-- FavoredReligions
--------------------------------------------------------------------------------------------------------------------------
INSERT OR REPLACE INTO FavoredReligions
		(LeaderType,			ReligionType)
VALUES	('LEADER_GA_DUARTE',	'RELIGION_CATHOLICISM');
--==========================================================================================================================
-- LEADERS: LOADING INFO
--==========================================================================================================================
-- LoadingInfo
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO LoadingInfo
(LeaderType,			BackgroundImage, 				ForegroundImage,			PlayDawnOfManAudio)
VALUES	('LEADER_GA_DUARTE',	'LEADER_DEFAULT_BACKGROUND',	'LEADER_GA_DUARTE_NEUTRAL',	0);
--==========================================================================================================================
-- LEADERS: TRAITS
--==========================================================================================================================
-- Types
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO Types
(Type,									Kind)
VALUES	('TRAIT_LEADER_GA_DUARTE_VISIONARY',	'KIND_TRAIT');
--------------------------------------------------------------------------------------------------------------------------
-- Traits
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO Traits
(TraitType,								Name,												Description)
VALUES	('TRAIT_LEADER_GA_DUARTE_VISIONARY',	'LOC_TRAIT_LEADER_GA_DUARTE_VISIONARY_NAME',		'LOC_TRAIT_LEADER_GA_DUARTE_VISIONARY_DESCRIPTION');
--------------------------------------------------------------------------------------------------------------------------
-- TraitModifiers
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO TraitModifiers
(TraitType,								ModifierId)
VALUES	('TRAIT_LEADER_GA_DUARTE_VISIONARY',	'GA_LEADER_DUARTE_VISIONARY_EXTRA_SLOT_1');
--------------------------------------------------------------------------------------------------------------------------
-- Modifiers
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO Modifiers
(ModifierId,								ModifierType)
VALUES	('GA_LEADER_DUARTE_VISIONARY_EXTRA_SLOT_1',	'MODIFIER_PLAYER_CULTURE_ADJUST_GOVERNMENT_SLOTS_MODIFIER');
--------------------------------------------------------------------------------------------------------------------------
-- ModifierArguments
--------------------------------------------------------------------------------------------------------------------------
INSERT INTO ModifierArguments
(ModifierId,								Name,					Value)
VALUES	('GA_LEADER_DUARTE_VISIONARY_EXTRA_SLOT_1',	'GovernmentSlotType',	'SLOT_WILDCARD');
--==========================================================================================================================
--==========================================================================================================================