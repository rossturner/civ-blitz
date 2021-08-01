INSERT INTO PlayerColors
(	Type,
     Usage,
     PrimaryColor,
     SecondaryColor,

     Alt1PrimaryColor,
     Alt1SecondaryColor,

     Alt2PrimaryColor,
     Alt2SecondaryColor,

     Alt3PrimaryColor,
     Alt3SecondaryColor)
SELECT 'LEADER_IMP_JAPABULLARABCREE',
    Usage,
    PrimaryColor,
    SecondaryColor,

    Alt1PrimaryColor,
    Alt1SecondaryColor,

    Alt2PrimaryColor,
    Alt2SecondaryColor,

    Alt3PrimaryColor,
    Alt3SecondaryColor
FROM PlayerColors
WHERE Type = 'LEADER_T_ROOSEVELT';