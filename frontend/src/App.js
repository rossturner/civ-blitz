import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import React, {useEffect, useState} from "react";
import CardStore from "./cards/CardStore";
import CivBuilder from "./CivBuilder";
import ImpRandom from "./ImpRandom";
import ModTester from "./ModTester";

const axios = require('axios');

function App() {

    const [currentPage, setCurrentPage] = useState('modtester');
    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
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
            <TopLevelMenu onItemClick={(name) => setCurrentPage(name)}/>


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
