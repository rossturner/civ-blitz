import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import React, {useEffect, useState} from "react";
import CardStore from "./cards/CardStore";
import CivBuilder from "./CivBuilder";
import ImpRandom from "./ImpRandom";
import ModTester from "./ModTester";
import PlayerCollection from "./PlayerCollection";
import jwt from "jsonwebtoken";

const axios = require('axios');

function App() {

    const [currentPage, setCurrentPage] = useState('modtester');
    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(true);
    const [loggedInPlayer, setLoggedInPlayer] = useState();

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        let jsonWebToken = urlParams.get('token');
        if (jsonWebToken) {
            window.localStorage.setItem('token', jsonWebToken);
        } else {
            jsonWebToken = window.localStorage.getItem('token');
            // TODO check for expiry and trigger refresh of token here
        }

        if (jsonWebToken) {
            axios.defaults.headers.common['Authorization'] = jsonWebToken;
            const decoded = jwt.decode(jsonWebToken);
            console.log('token: ', decoded);

            setLoggedInPlayer({
                discordUsername: decoded.username,
                discordId: decoded.sub
            });
            setCurrentPage('collection');
        }


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
