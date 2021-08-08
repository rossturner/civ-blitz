import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import React, {useEffect, useState} from "react";
import CardStore from "./cards/CardStore";
import CivBuilder from "./CivBuilder";
import ImpRandom from "./ImpRandom";
import ModTester from "./ModTester";
import PlayerCollection from "./PlayerCollection";

const axios = require('axios');

function App() {

    const [currentPage, setCurrentPage] = useState('modtester');
    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(true);
    const [loggedInPlayer, setLoggedInPlayer] = useState();

    useEffect(() => {
        axios.get("/api/cards")
            .then((response) => {
                if (!CardStore.initialised) {
                    CardStore.addCards(response.data);
                    let newCollection = CardStore.getInitialCollection();
                    newCollection.sort(ImpRandom.cardSort);
                    setLoading(false);
                    setCollection(newCollection);
                }
            })
            .catch((error) => {
                console.error('Error loading cards', error);
            });

        axios.get("/api/player")
            .then((response) => {
                if (response.data) {
                    console.log('Retrieved logged in player', response.data);
                    setLoggedInPlayer(response.data)
                    setCurrentPage('collection');
                }
            })
            .catch((error) => {
                console.error('Error retrieving login state (expecting 204 when not logged in)', error);
            })
    }, []);

    if (loading) {
        return <div>Loading...</div>
    }
    return (
        <div>
            <TopLevelMenu loggedInPlayer={loggedInPlayer} onItemClick={(name) => setCurrentPage(name)}/>

            {currentPage === 'collection' &&
            <PlayerCollection loggedInPlayer={loggedInPlayer} />
            }

            {currentPage === 'civbuilder' &&
            <CivBuilder collection={collection} setCollection={setCollection}/>
            }

            {currentPage === 'modtester' &&
            <ModTester/>
            }

        </div>
    );
}

export default App;
