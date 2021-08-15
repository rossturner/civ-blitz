

const CardInfo = {

    getCategoryName: (category) => {
        switch (category) {
            case 'CivilizationAbility':
                return 'Civilization Ability';
            case 'LeaderAbility':
                return 'Leader Ability';
            case 'UniqueUnit':
                return 'Unique Unit';
            case 'UniqueInfrastructure':
                return 'Unique Infrastructure';
            default:
                return 'Unknown';
        }
    },

    getSignupPropName: (category) => {
        switch (category) {
            case 'CivilizationAbility':
                return 'cardCivAbility';
            case 'LeaderAbility':
                return 'cardLeaderAbility';
            case 'UniqueUnit':
                return 'cardUniqueUnit';
            case 'UniqueInfrastructure':
                return 'cardUniqueInfrastruture';
            default:
                return 'Unknown';
        }
    },

    getCategoryColor: (category) => {
        switch (category) {
            case 'CivilizationAbility':
                return 'purple';
            case 'LeaderAbility':
                return 'blue';
            case 'UniqueUnit':
                return 'red';
            case 'UniqueInfrastructure':
                return 'green';
            default:
                return 'grey';
        }
    }

};

export default CardInfo;