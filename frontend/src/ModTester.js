import React, {useEffect, useState} from "react";
import ImpRandom from "./ImpRandom";
import CardStore, {MAIN_CATEGORIES} from "./cards/CardStore";
import ConstructedCiv from "./ConstructedCiv";
import {Container, Header} from "semantic-ui-react";
import CivCardGroup from "./cards/CivCardGroup";

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
        const maxOneOfCategory = MAIN_CATEGORIES.includes(card.cardCategory);

        let editedCards = editingCiv.cards;
        let updatedCollection = [].concat(collection).filter(c => c !== card);

        if (maxOneOfCategory) {
            const toReplaceInCiv = editingCiv.cards.filter(selected => selected.cardCategory === card.cardCategory);

            editedCards = editingCiv.cards.filter(selected => selected.cardCategory !== card.cardCategory);
            editedCards.push(card);
            editedCards.sort(ImpRandom.cardSort);

            updatedCollection = updatedCollection.concat(toReplaceInCiv);
        } else {
            editedCards.push(card);
            editedCards.sort(ImpRandom.cardSort);
        }

        updatedCollection.sort(ImpRandom.cardSort);
        setCollection(updatedCollection);

        setEditingCiv({
            cards: editedCards,
            editable: true
        });
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
            <Container>
                <ConstructedCiv index='mod tester' cards={editingCiv.cards} editable={editingCiv.editable} alwaysEditing={true}
                                onCardClick={(card) => civCardClicked(card)} />
            </Container>

            <Container style={{marginTop: '2em'}}>
                <Header as='h2'>All cards</Header>
                <CivCardGroup cards={collection} cardClicked={collectionCardClicked} />
            </Container>
        </React.Fragment>
    );
};

export default ModTester;