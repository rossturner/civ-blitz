import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import {Card, Container, Header} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import ImperiumCard from "./cards/ImperiumCard";
import ConstructedCiv from "./ConstructedCiv";
import CardStore from "./cards/CardStore";

function App() {

    const [storedCivilizations, setStoredCivilizations] = useState([]);
    const [editingCiv, setEditingCiv] = useState({
        cards: [],
        editable: true
    });
    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(true);

    const cardSort = (a, b) => {
        return a.cardCategory.localeCompare(b.cardCategory) || a.cardName.localeCompare(b.cardName);
    }

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
                            newCollection.sort(cardSort);
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

    const collectionCardClicked = (card) => {
        if (!editingCiv) {
            return;
        }
        const toReplaceInCiv = editingCiv.cards.filter(selected => selected.cardCategory === card.cardCategory);

        let editedCards = editingCiv.cards.filter(selected => selected.cardCategory !== card.cardCategory);
        editedCards.push(card);
        editedCards.sort(cardSort);

        let updatedCollection = [].concat(collection).filter(c => c !== card);
        updatedCollection = updatedCollection.concat(toReplaceInCiv);
        updatedCollection.sort(cardSort);

        setEditingCiv({
            cards: editedCards,
            editable: true
        });
        setCollection(updatedCollection);
    };
    const civCardClicked = (card) => {

        let editedCards = editingCiv.cards.filter(c => c !== card);
        editedCards.sort(cardSort);

        let updatedCollection = [].concat(collection);
        updatedCollection.push(card);
        updatedCollection.sort(cardSort);

        setEditingCiv({
            cards: editedCards,
            editable: true
        });
        setCollection(updatedCollection);
    };
    const civConfirmed = () => {
        const updatedCivs = [].concat(storedCivilizations);
        updatedCivs.push({
            cards: editingCiv.cards,
            editiable: false
        });
        setStoredCivilizations(updatedCivs);

        let updatedCollection = [].concat(collection).concat(CardStore.getMoreCardsForCollection(collection));
        updatedCollection.sort(cardSort);
        setCollection(updatedCollection);

        if (updatedCivs.length < 3) {
            setEditingCiv({
                cards: [],
                editable: true
            });
        } else {
            setEditingCiv(null);
        }
    };

    const cardItems = collection.map((cardJson, index) => {
        return (<ImperiumCard key={index} cardJson={cardJson} onClick={() => collectionCardClicked(cardJson)}/>);
    })
    let civilizations = [].concat(storedCivilizations);
    if (editingCiv) {
        civilizations.push(editingCiv);
    }
    const civItems = civilizations.map((civ, index) => {
        return (<ConstructedCiv key={index} index={index + 1} cards={civ.cards} editable={civ.editable}
                                onCardClick={(card) => civCardClicked(card)} onConfirmClick={() => civConfirmed()}  />);
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
