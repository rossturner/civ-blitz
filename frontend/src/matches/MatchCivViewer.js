import {Card, CardGroup, Container, Placeholder} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import CardInfo from "../cards/CardInfo";
import CardStore, {CATEGORIES} from "../cards/CardStore";
import ImperiumCard from "../cards/ImperiumCard";
import ObjectiveCard from "./objectives/ObjectiveCard";
import axios from "axios";


const MatchCivViewer = ({signup, loggedInPlayer}) => {

    const [secretObjectives, setSecretObjectives] = useState([]);

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

    const civItems = CATEGORIES.map(category => {
        const propName = CardInfo.getSignupPropName(category);
        if (signup[propName]) {
            const card = CardStore.getCardByTraitType(signup[propName]);
            return <ImperiumCard cardJson={card} clickDisabled={true}/>;
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

    return (
        <React.Fragment>
            <Container>
                <Container style={{'marginBottom': '1em'}}>
                    Start bias: {CardStore.getMediaNameForCivType(signup.startBiasCivType)}
                </Container>
                {secretObjectives.length > 0 &&
                <CardGroup centered>
                    {secretObjectives.filter(o => o.selected).map(s => <ObjectiveCard key={s.objectiveName}
                                                                                      objectiveJson={s}
                                                                                      clickDisabled={true}/>)}
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