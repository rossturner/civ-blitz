import {Button, Card, Container, Header, Segment} from "semantic-ui-react";
import React from "react";
import ImperiumCard from "./cards/ImperiumCard";

const ConstructedCiv = ({index, cards, editable, onCardClick, onConfirmClick, alwaysEditing}) => {

    const cardItems = cards.map((card, index) => {
        return <ImperiumCard key={index} cardJson={card} onClick={() => editable ? onCardClick(card) : {}}/>;
    });

    let downloadLink = "/api/mods?";

    const searchParams = new URLSearchParams();
    if (cards) {
        cards.forEach(card => searchParams.append('traitType', card.traitType));
    }
    downloadLink += searchParams.toString();

    return (
        <Segment>
            <Header as='h3'>Civ {index}</Header>

            {cards.length === 0 &&
            <p>Add some cards to get started</p>
            }

            <Card.Group>
                {cardItems}
            </Card.Group>


            <Container style={{'padding-top': '2em'}}>
                {editable && !alwaysEditing &&
                <Button primary disabled={cards.length < 4} onClick={() => onConfirmClick()}>
                    Confirm
                </Button>
                }

                {(!editable || (alwaysEditing && cards.length === 4)) &&
                <Button as='a' href={downloadLink}>
                    Download mod
                </Button>
                }
            </Container>
        </Segment>
    );
};

export default ConstructedCiv;