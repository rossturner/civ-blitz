import {Button, Header, Segment} from "semantic-ui-react";
import React from "react";
import ImperiumCard from "./cards/ImperiumCard";


const ConstructedCiv = ({index, cards, editable, onCardClick}) => {

    const cardItems = cards.map((card, index) => {
        return <ImperiumCard key={index} cardJson={card} onClick={() => editable ? onCardClick(card) : {}} />;
    });

    return (
        <Segment>
            <Header as='h3'>Civ {index}</Header>

            {cards.length === 0 &&
            <p>Add some cards to get started</p>
            }

            {cardItems}

            {editable &&
            <Button disabled>
                Confirm
            </Button>
            }
        </Segment>
    );
};

export default ConstructedCiv;