

-- Using Japan CA, Bullmoose Teddy, Arabia UI, Cree UU

INSERT OR REPLACE INTO Types (Type, Kind)
VALUES	('CIVILIZATION_IMP_JAPABULLARABCREE',	'KIND_CIVILIZATION');

-- Leader, needs LeaderType and that leader's origin civ to avoid duplicates
-- Looks like we'll probably have to create a new leader with the same traits, as that's how Kublai and Eleanor work

INSERT OR REPLACE INTO CivilizationLeaders
(CivilizationType, LeaderType, CapitalName)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', 'LEADER_T_ROOSEVELT', CivilizationLeaders.CapitalName
FROM CivilizationLeaders
WHERE CivilizationLeaders.LeaderType = 'LEADER_T_ROOSEVELT'
  and CivilizationLeaders.CivilizationType = 'CIVILIZATION_AMERICA';

-- following from vanilla civ selected for names (Civ Ability civ?)

INSERT OR REPLACE INTO Civilizations
(CivilizationType, Name, Description, Adjective, StartingCivilizationLevelType,	RandomCityNameDepth, Ethnicity)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', Civilizations.Name, Civilizations.Description, Civilizations.Adjective,
       Civilizations.StartingCivilizationLevelType,  Civilizations.RandomCityNameDepth, Civilizations.Ethnicity
FROM Civilizations
WHERE Civilizations.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO CityNames (CivilizationType, CityName)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', CityNames.CityName
from CityNames
where CityNames.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO CivilizationCitizenNames
(CivilizationType, CitizenName,	Female, Modern)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', CivilizationCitizenNames.CitizenName, CivilizationCitizenNames.Female, CivilizationCitizenNames.Modern
FROM CivilizationCitizenNames
WHERE CivilizationCitizenNames.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO CivilizationInfo
(CivilizationType, Header, Caption, SortIndex)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', CivilizationInfo.Header, CivilizationInfo.Caption, CivilizationInfo.SortIndex
FROM CivilizationInfo
WHERE CivilizationInfo.CivilizationType = 'CIVILIZATION_JAPAN';

-- Following from start bias civ

INSERT OR REPLACE INTO StartBiasFeatures
(CivilizationType, FeatureType, Tier)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', StartBiasFeatures.FeatureType, StartBiasFeatures.Tier
FROM StartBiasFeatures
WHERE StartBiasFeatures.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO StartBiasResources
(CivilizationType, ResourceType, Tier)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', StartBiasResources.ResourceType, StartBiasResources.Tier
FROM StartBiasResources
WHERE StartBiasResources.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO StartBiasRivers
(CivilizationType, Tier)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', StartBiasRivers.Tier
FROM StartBiasRivers
WHERE StartBiasRivers.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO StartBiasTerrains
(CivilizationType, TerrainType, Tier)
SELECT 'CIVILIZATION_IMP_JAPABULLARABCREE', StartBiasTerrains.TerrainType, StartBiasTerrains.Tier
FROM StartBiasTerrains
WHERE StartBiasTerrains.CivilizationType = 'CIVILIZATION_JAPAN';
