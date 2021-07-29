

export const CATEGORIES = ["CivilizationAbility", "", "UniqueUnit", "UniqueInfrastructure"];
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