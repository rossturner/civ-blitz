import React, {useEffect, useState} from "react";
import ImpRandom from "./ImpRandom";
import CardStore from "./cards/CardStore";
import ConstructedCiv from "./ConstructedCiv";
import {Container, Header} from "semantic-ui-react";
import ImperiumCardGroup from "./cards/ImperiumCardGroup";

const ModTester = () => {
    const [editingCiv, setEditingCiv] = useState({
        cards: [],
        editable: true
    });

    const [collection, setCollection] = useState([]);

    useEffect(() => {
        const allCards = CardStore.getAll();
        let cardsClone = [].concat(allCards);
        cardsClone.sort(ImpRandom.cardSort);
        setCollection(cardsClone);
    }, []);

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

    return (
        <React.Fragment>
            <Container style={{marginTop: '6em'}}>
                <ConstructedCiv index='mod tester' cards={editingCiv.cards} editable={editingCiv.editable} alwaysEditing={true}
                                onCardClick={(card) => civCardClicked(card)} />
            </Container>

            <Container style={{marginTop: '2em'}}>
                <Header as='h2'>All cards</Header>
                <ImperiumCardGroup cards={collection} cardClicked={collectionCardClicked} />
            </Container>
        </React.Fragment>
    );
};

export default ModTester;