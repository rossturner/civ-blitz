import machineId from './lib/machine-id';
import seedrandom from 'seedrandom';

let myMachineId = machineId();
if (myMachineId === '0d4ae59d523dbcf596c52badd3415c1b') {
    myMachineId = seedrandom().int32();
}
const rnd = seedrandom(myMachineId);

const ImpRandom = {

    getRandomInt: (exclusiveLimit) => {
        return Math.floor(rnd() * (exclusiveLimit ));
    }

};

export default ImpRandom;