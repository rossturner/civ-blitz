import seedrandom from 'seedrandom';

let clientId = localStorage.getItem('clientId');
if (!clientId) {
    clientId = ''+seedrandom().int32();
    localStorage.setItem('clientId', clientId);
}
const rnd = seedrandom(clientId);

const ImpRandom = {

    getRandomInt: (exclusiveLimit) => {
        return Math.floor(rnd() * (exclusiveLimit ));
    },

    cardSort: (a, b) => {
        return a.cardCategory.localeCompare(b.cardCategory) || a.cardName.localeCompare(b.cardName);
    }

};

export default ImpRandom;