import {Button, Card, Container, Header, Segment} from "semantic-ui-react";
import React from "react";
import ImperiumCard from "./cards/ImperiumCard";


const ConstructedCiv = ({index, cards, editable, onCardClick, onConfirmClick}) => {

    const cardItems = cards.map((card, index) => {
        return <ImperiumCard key={index} cardJson={card} onClick={() => editable ? onCardClick(card) : {}}/>;
    });

    return (
        <Segment>
            <Header as='h3'>Civ {index}</Header>

            {cards.length === 0 &&
            <p>Add some cards to get started</p>
            }

            <Card.Group>
                {cardItems}
            </Card.Group>


            {editable &&
            <Container style={{'padding-top': '2em'}}>
                <Button primary disabled={cards.length < 4} onClick={() => onConfirmClick()}>
                    Confirm
                </Button>
            </Container>
            }
        </Segment>
    );
};

export default ConstructedCiv;