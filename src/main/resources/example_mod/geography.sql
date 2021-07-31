INSERT OR REPLACE INTO NamedRiverCivilizations (NamedRiverType, CivilizationType)
SELECT NamedRiverCivilizations.NamedRiverType, 'CIVILIZATION_IMP_JAPABULLARABCREE'
FROM NamedRiverCivilizations
WHERE NamedRiverCivilizations.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO NamedMountainCivilizations (NamedMountainType, CivilizationType)
SELECT NamedMountainCivilizations.NamedMountainType, 'CIVILIZATION_IMP_JAPABULLARABCREE'
FROM NamedMountainCivilizations
WHERE NamedMountainCivilizations.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO NamedVolcanoCivilizations (NamedVolcanoType, CivilizationType)
SELECT NamedVolcanoCivilizations.NamedVolcanoType, 'CIVILIZATION_IMP_JAPABULLARABCREE'
FROM NamedVolcanoCivilizations
WHERE NamedVolcanoCivilizations.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO NamedDesertCivilizations (NamedDesertType, CivilizationType)
SELECT NamedDesertCivilizations.NamedDesertType, 'CIVILIZATION_IMP_JAPABULLARABCREE'
FROM NamedDesertCivilizations
WHERE NamedDesertCivilizations.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO NamedLakeCivilizations (NamedLakeType, CivilizationType)
SELECT NamedLakeCivilizations.NamedLakeType, 'CIVILIZATION_IMP_JAPABULLARABCREE'
FROM NamedLakeCivilizations
WHERE NamedLakeCivilizations.CivilizationType = 'CIVILIZATION_JAPAN';

INSERT OR REPLACE INTO NamedSeaCivilizations (NamedSeaType, CivilizationType)
SELECT NamedSeaCivilizations.NamedSeaType, 'CIVILIZATION_IMP_JAPABULLARABCREE'
FROM NamedSeaCivilizations
WHERE NamedSeaCivilizations.CivilizationType = 'CIVILIZATION_JAPAN';
