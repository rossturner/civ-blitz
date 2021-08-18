import {Card, Container, Placeholder} from "semantic-ui-react";
import React from "react";
import CardInfo from "../cards/CardInfo";
import CardStore, {CATEGORIES} from "../cards/CardStore";
import ImperiumCard from "../cards/ImperiumCard";


const MatchCivBuilder = ({signup}) => {


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
                <Card.Group>
                    {civItems}
                </Card.Group>
            </Container>
        </React.Fragment>
    );
}

export default MatchCivBuilder;