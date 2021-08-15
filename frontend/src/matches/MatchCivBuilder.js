import {Card, Container, Header, Placeholder} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import ImperiumCardGroup from "../cards/ImperiumCardGroup";
import axios from "axios";
import ImpRandom from "../ImpRandom";
import CardInfo from "../cards/CardInfo";
import CardStore, {CATEGORIES} from "../cards/CardStore";
import ImperiumCard from "../cards/ImperiumCard";


const MatchCivBuilder = ({match, loggedInPlayer, onCommitChange}) => {

    const [loading, setLoading] = useState(true);
    const [collection, setCollection] = useState([]);
    const [currentPlayerSignup, setCurrentPlayerSignup] = useState({});


    useEffect(() => {
        axios.get('/api/player/collection')
            .then((response) => {
                response.data.sort(ImpRandom.cardSort);
                setCollection(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Error retrieving match', error);
            })
    }, [loading]);

    useEffect(() => {
        setCurrentPlayerSignup(match.signups.find(s => s.playerId === loggedInPlayer.discordId));
    }, [match, loggedInPlayer]);

    const collectionCardClicked = (card) => {
        axios.post('/api/matches/'+match.matchId+'/cards', {cardTraitType: card.traitType})
            .then(response => {
                setCurrentPlayerSignup(response.data);
                setLoading(true);
            })
    };
    const civCardClicked = (card) => {
        console.log('card', card);
        axios.post('/api/matches/'+match.matchId+'/cards/remove', {cardTraitType: card.traitType})
            .then(response => {
                setCurrentPlayerSignup(response.data);
                setLoading(true);
            })
    };
    const civConfirmed = () => {
        // const updatedCivs = [].concat(storedCivilizations);
        // updatedCivs.push({
        //     cards: editingCiv.cards,
        //     editiable: false
        // });
        // setStoredCivilizations(updatedCivs);
        //
        // let updatedCollection = [].concat(collection).concat(CardStore.getMoreCardsForCollection(collection));
        // updatedCollection.sort(ImpRandom.cardSort);
        // setCollection(updatedCollection);
        //
        // if (updatedCivs.length < 3) {
        //     setEditingCiv({
        //         cards: [],
        //         editable: true
        //     });
        // } else {
        //     setEditingCiv(null);
        // }
    };

    const civItems = CATEGORIES.map(category => {
        const propName = CardInfo.getSignupPropName(category);
        if (currentPlayerSignup[propName]) {
            const card = CardStore.getCardByTraitType(currentPlayerSignup[propName]);
            return <ImperiumCard cardJson={card} onClick={() => civCardClicked(card)} />;
        } else {
            return <Card
                className='imperium-card'
                color={CardInfo.getCategoryColor(category)}
                meta={CardInfo.getCategoryName(category)}
                description={(<Placeholder>
                    <Placeholder.Image />
                    <Placeholder.Paragraph>
                        <Placeholder.Line />
                        <Placeholder.Line />
                        <Placeholder.Line />
                        <Placeholder.Line />
                    </Placeholder.Paragraph>
                </Placeholder>)}
            />;
        }
    });

    return (
        <React.Fragment>
            <Container>
                <Card.Group>
                    {civItems}
                </Card.Group>
            </Container>

            <Container style={{marginTop: '1em'}}>
                <Header as='h3'>Available collection</Header>
                <ImperiumCardGroup cards={collection} cardClicked={collectionCardClicked} />
            </Container>
        </React.Fragment>
    );
}

export default MatchCivBuilder;