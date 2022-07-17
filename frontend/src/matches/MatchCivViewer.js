import {Card, CardGroup, Container, Placeholder} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import CardInfo from "../cards/CardInfo";
import CardStore, {MAIN_CATEGORIES} from "../cards/CardStore";
import CivCard from "../cards/CivCard";
import ObjectiveCard from "./objectives/ObjectiveCard";
import axios from "axios";


const MatchCivViewer = ({match, signup, loggedInPlayer, secretObjectivesProp, secretObjectiveClicked}) => {

    const [secretObjectives, setSecretObjectives] = useState(secretObjectivesProp || []);

    useEffect(() => {
        if (signup.playerId === loggedInPlayer.discordId) {
            axios.get('/api/matches/' + signup.matchId + '/secret_objectives')
                .then((response) => {
                    setSecretObjectives(response.data);
                })
                .catch((error) => {
                    console.error('Error retrieving secret objectives', error);
                })
        }
    }, [signup, loggedInPlayer]);

    useEffect(() => {
        if (secretObjectivesProp.length > 0) {
            setSecretObjectives(secretObjectivesProp);
        }
    }, [secretObjectivesProp]);

    const civItems = MAIN_CATEGORIES.map(category => {
        let cardForCategory = signup.selectedCards.find(c => c.cardCategory === category);
        if (cardForCategory) {
            return <CivCard key={category} cardJson={cardForCategory} clickDisabled={true}/>;
        } else {
            return <Card
                key={category}
                className='civ-card'
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
    signup.selectedCards.forEach(card => {
        if (!MAIN_CATEGORIES.includes(card.cardCategory)) {
            civItems.push(<CivCard key={card.identifier} cardJson={card} clickDisabled={true}/>);
        }
    })

    return (
        <React.Fragment>
            <Container>
                <Container style={{'marginBottom': '1em'}}>
                    Start bias: {CardStore.getMediaNameForCivType(signup.startBiasCivType)}
                </Container>
                {secretObjectives.length > 0 &&
                <CardGroup centered>
                    {secretObjectives.filter(o => o.selected).map(s =>
                        <ObjectiveCard key={s.objectiveName}
                                       objectiveJson={s}
                                       cardClicked={secretObjectiveClicked}
                                       clickDisabled={signup.playerId !== loggedInPlayer.discordId || match.matchState !== 'IN_PROGRESS'}
                                       claimedByPlayers={s.claimed ? [signup] : []}
                        />)}
                </CardGroup>
                }
                <Card.Group>
                    {civItems}
                </Card.Group>
            </Container>
        </React.Fragment>
    );
}

export default MatchCivViewer;