import ImpRandom from "../ImpRandom";

let allCards = [];

export const CATEGORIES = ["CivilizationAbility", "LeaderAbility", "UniqueInfrastructure", "UniqueUnit"];

const byCategory = {};
const byTraitType = {};
const mediaByCivType = {};

CATEGORIES.forEach(cat => {
    byCategory[cat] = [];
})

function getUniqueCardsFromCategory(category, numCards, collection = []) {
    let selected = [];
    while (selected.length < numCards) {
        const possibleCard = byCategory[category][ImpRandom.getRandomInt(byCategory[category].length)];
        if (!selected.includes(possibleCard) && !collection.includes(possibleCard)) {
            selected.push(possibleCard);
        }
    }
    return selected;
}

const CardStore = {

    initialised: false,

    addCards: (cards) => {
        if (!CardStore.initialised) {
            allCards = allCards.concat(cards);
            cards.forEach(card => {
                byCategory[card.cardCategory].push(card);
                byTraitType[card.traitType] = card;
                if (card.cardCategory === 'CivilizationAbility') {
                    mediaByCivType[card.civilizationType] = card.mediaName;
                }
            });
            CardStore.initialised = true;
        }
    },

    getAll: () => {
        return allCards;
    },

    getMediaNameForCivType: (civType) => {
        return mediaByCivType[civType];
    },

    getCardByTraitType: (traitType) => {
        return byTraitType[traitType];
    }
};

export default CardStore;