import seedrandom from 'seedrandom';

let clientId = localStorage.getItem('clientId');
if (!clientId) {
    clientId = ''+seedrandom().int32();
    localStorage.setItem('clientId', clientId);
}
console.log('clientId', clientId);
const rnd = seedrandom(clientId);

const ImpRandom = {

    getRandomInt: (exclusiveLimit) => {
        return Math.floor(rnd() * (exclusiveLimit ));
    }

};

export default ImpRandom;