import ImpRandom from "../ImpRandom";

let allCards = [];

export const CATEGORIES = ["CivilizationAbility", "LeaderAbility", "UniqueUnit", "UniqueInfrastructure"];

const byCategory = {};

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
            });
            CardStore.initialised = true;
        }
    },

    getAll: () => {
        return allCards;
    },

    getInitialCollection: () => {
        let selected = [];
        CATEGORIES.forEach(category => {
            selected = selected.concat(getUniqueCardsFromCategory(category, 4));
        })
        return selected;
    },

    getMoreCardsForCollection: (collection) => {
        let selected = [];
        CATEGORIES.forEach(category => {
            selected = selected.concat(getUniqueCardsFromCategory(category, 2, collection));
        })
        return selected;
    }

};

export default CardStore;