const allCards = [];

export const CATEGORIES = ["CivilizationAbility", "LeaderAbility", "UniqueUnit", "UniqueInfrastructure"];

const byCategory = {};

CATEGORIES.forEach(cat => {
    byCategory[cat] = [];
})

function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

function getUniqueCardsFromCategory(category, numCards) {
    let selected = [];
    while (selected.length < numCards) {
        const possibleCard = byCategory[category][getRandomInt(byCategory[category].length)];
        if (!selected.includes(possibleCard)) {
            selected.push(possibleCard);
        }
    }
    return selected;
}

const CardStore = {

    initialised: false,

    addCards: (cards) => {
        if (!CardStore.initialised) {
            allCards.concat(cards);
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
    }

};

export default CardStore;