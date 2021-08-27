import {Card, CardGroup, Container, Placeholder} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import CardInfo from "../cards/CardInfo";
import CardStore, {CATEGORIES} from "../cards/CardStore";
import ImperiumCard from "../cards/ImperiumCard";
import ObjectiveCard from "./objectives/ObjectiveCard";
import axios from "axios";


const MatchCivViewer = ({signup, loggedInPlayer, secretObjectivesProp, secretObjectiveClicked}) => {

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

    console.log('secretObjectives', secretObjectives);
    const civItems = CATEGORIES.map(category => {
        const propName = CardInfo.getSignupPropName(category);
        if (signup[propName]) {
            const card = CardStore.getCardByTraitType(signup[propName]);
            return <ImperiumCard key={category} cardJson={card} clickDisabled={true}/>;
        } else {
            return <Card
                key={category}
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
                                       clickDisabled={!secretObjectiveClicked}
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