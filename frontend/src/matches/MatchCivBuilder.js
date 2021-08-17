import {Button, Card, Container, Header, List, Placeholder, Select} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import ImperiumCardGroup from "../cards/ImperiumCardGroup";
import axios from "axios";
import ImpRandom from "../ImpRandom";
import CardInfo from "../cards/CardInfo";
import CardStore, {CATEGORIES} from "../cards/CardStore";
import ImperiumCard from "../cards/ImperiumCard";


const MatchCivBuilder = ({match, loggedInPlayer, onCommitChange}) => {

    const [loadingCollection, setLoadingCollection] = useState(true);
    const [collection, setCollection] = useState([]);
    const [currentPlayerSignup, setCurrentPlayerSignup] = useState({});

    useEffect(() => {
        axios.get('/api/player/collection')
            .then((response) => {
                response.data.sort(ImpRandom.cardSort);
                setCollection(response.data);
                setLoadingCollection(false);
            })
            .catch((error) => {
                console.error('Error retrieving match', error);
            })
    }, [loadingCollection]);

    useEffect(() => {
        setCurrentPlayerSignup(match.signups.find(s => s.playerId === loggedInPlayer.discordId));
    }, [match, loggedInPlayer]);

    const collectionCardClicked = (card) => {
        axios.post('/api/matches/' + match.matchId + '/cards', {cardTraitType: card.traitType})
            .then(response => {
                setCurrentPlayerSignup(response.data);
                setLoadingCollection(true);
            })
            .catch(console.error)
    };
    const civCardClicked = (card) => {
        if (!currentPlayerSignup.committed) {
            axios.post('/api/matches/' + match.matchId + '/cards/remove', {cardTraitType: card.traitType})
                .then(response => {
                    setCurrentPlayerSignup(response.data);
                    setLoadingCollection(true);
                })
                .catch(console.error)
        }
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

    const startBiases = {};
    CATEGORIES.forEach(category => {
        const propName = CardInfo.getSignupPropName(category);
        if (currentPlayerSignup[propName]) {
            const card = CardStore.getCardByTraitType(currentPlayerSignup[propName]);
            startBiases[card.civilizationType] = card.civilizationFriendlyName;
        }
    });
    const startBiasOptions = [];
    for (const [civType, friendlyName] of Object.entries(startBiases)) {
        startBiasOptions.push({
            key: civType,
            value: civType,
            text: friendlyName
        });
    }

    const startBiasChanged = (newStartBias) => {
        axios.post('/api/matches/' + match.matchId + '/bias', {startBiasCivType: newStartBias})
            .then(response => {
                setCurrentPlayerSignup(response.data);
            })
            .catch(console.error)
    };


    const civItems = CATEGORIES.map(category => {
        const propName = CardInfo.getSignupPropName(category);
        if (currentPlayerSignup[propName]) {
            const card = CardStore.getCardByTraitType(currentPlayerSignup[propName]);
            return <ImperiumCard cardJson={card} onClick={civCardClicked}
                                 clickDisabled={currentPlayerSignup.committed}/>;
        } else {
            return <Card
                className='imperium-card'
                color={CardInfo.getCategoryColor(category)}
                meta={CardInfo.getCategoryName(category)}
                description={(<Placeholder>
                    <Placeholder.Image/>
                    <Placeholder.Paragraph>
                        <Placeholder.Line/>
                        <Placeholder.Line/>
                        <Placeholder.Line/>
                        <Placeholder.Line/>
                    </Placeholder.Paragraph>
                </Placeholder>)}
            />;
        }
    });
    const canCommit = !currentPlayerSignup.committed && currentPlayerSignup.startBiasCivType &&
        CATEGORIES.every(category => currentPlayerSignup[CardInfo.getSignupPropName(category)]);

    const doCommit = () => {
        axios.post('/api/matches/' + match.matchId + '/commit')
            .then(response => {
                setCurrentPlayerSignup(response.data);
                onCommitChange();
            })
            .catch(console.error)
    }

    const undoCommit = () => {
        axios.delete('/api/matches/' + match.matchId + '/commit')
            .then(response => {
                setCurrentPlayerSignup(response.data);
                onCommitChange();
            })
            .catch(console.error)
    }

    return (
        <React.Fragment>
            <Container>

                <Container style={{'marginBottom': '1em'}}>
                    <List horizontal>
                        {startBiasOptions.length > 0 &&
                        <List.Item>
                            <Select placeholder='Select start bias...' value={currentPlayerSignup.startBiasCivType}
                                    disabled={currentPlayerSignup.committed}
                                    options={startBiasOptions} onChange={(event, {value}) => startBiasChanged(value)}/>
                        </List.Item>
                        }
                        {canCommit &&
                        <List.Item>
                            <Button primary onClick={doCommit}>Commit</Button>
                        </List.Item>
                        }
                        {currentPlayerSignup.committed &&
                        <List.Item>
                            <Button negative onClick={undoCommit}>Uncommit</Button>
                        </List.Item>
                        }
                    </List>
                </Container>
                <Card.Group>
                    {civItems}
                </Card.Group>
            </Container>

            {!currentPlayerSignup.committed &&
            <Container style={{marginTop: '1em'}}>
                <Header as='h3'>Available collection</Header>
                <ImperiumCardGroup cards={collection} cardClicked={collectionCardClicked}/>
            </Container>
            }
        </React.Fragment>
    );
}

export default MatchCivBuilder;