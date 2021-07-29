import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import {Card, Container, Header} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import ImperiumCard from "./cards/ImperiumCard";
import ConstructedCiv from "./ConstructedCiv";
import CardStore from "./cards/CardStore";

function App() {

    const [civilizations, setCivilizations] = useState([{
        cards: [],
        editable: false
    }, {
        cards: [],
        editable: true
    }]);
    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (loading) {
            fetch("/example/cards")
                .then(res => res.json())
                .then(
                    (cards) => {
                        if (!CardStore.initialised) {
                            console.log('result', cards);
                            CardStore.addCards(cards);
                            let newCollection = CardStore.getInitialCollection();
                            setCollection(newCollection);
                            setLoading(false);
                        }
                    },
                    (error) => {
                        console.error('Error loading cards', error);
                    }
                );
        }
    });

    const cardItems = collection.map((cardJson, index) => {
        return (<ImperiumCard key={index} cardJson={cardJson}/>);
    })
    const civItems = civilizations.map((civ, index) => {
        return (<ConstructedCiv key={index} index={index + 1} cards={civ.cards} editable={civ.editable}/>);
    })

    return (
        <div>
            <TopLevelMenu/>

            {/*{loading &&*/}
            {/*<p>Loading</p>*/}
            {/*}*/}

            {/*{!loading &&*/}
            <React.Fragment>
                <Container style={{marginTop: '6em'}}>
                    {civItems}
                </Container>

                <Container style={{marginTop: '2em'}}>
                    <Header as='h2'>Available collection</Header>

                    <Card.Group>
                        {cardItems}
                    </Card.Group>

                </Container>
            </React.Fragment>
            {/*}*/}

        </div>
    );
}

export default App;
