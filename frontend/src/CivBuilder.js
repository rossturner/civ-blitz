import {Card, Container, Header} from "semantic-ui-react";
import React, {useState} from "react";
import CardStore from "./cards/CardStore";
import ImperiumCard from "./cards/ImperiumCard";
import ConstructedCiv from "./ConstructedCiv";
import ImpRandom from "./ImpRandom";


const CivBuilder = ({collection, setCollection}) => {

    const [storedCivilizations, setStoredCivilizations] = useState([]);
    const [editingCiv, setEditingCiv] = useState({
        cards: [],
        editable: true
    });

    const collectionCardClicked = (card) => {
        if (!editingCiv) {
            return;
        }
        const toReplaceInCiv = editingCiv.cards.filter(selected => selected.cardCategory === card.cardCategory);

        let editedCards = editingCiv.cards.filter(selected => selected.cardCategory !== card.cardCategory);
        editedCards.push(card);
        editedCards.sort(ImpRandom.cardSort);

        let updatedCollection = [].concat(collection).filter(c => c !== card);
        updatedCollection = updatedCollection.concat(toReplaceInCiv);
        updatedCollection.sort(ImpRandom.cardSort);

        setEditingCiv({
            cards: editedCards,
            editable: true
        });
        setCollection(updatedCollection);
    };
    const civCardClicked = (card) => {

        let editedCards = editingCiv.cards.filter(c => c !== card);
        editedCards.sort(ImpRandom.cardSort);

        let updatedCollection = [].concat(collection);
        updatedCollection.push(card);
        updatedCollection.sort(ImpRandom.cardSort);

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
        updatedCollection.sort(ImpRandom.cardSort);
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
    );
}

export default CivBuilder;