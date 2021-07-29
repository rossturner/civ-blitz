import {Button, Header, Segment} from "semantic-ui-react";
import React from "react";


const ConstructedCiv = ({index, cards, editable}) => {

    return (
        <Segment>
            <Header as='h3'>Civ {index}</Header>

            {cards.length === 0 &&
            <p>Add some cards to get started</p>
            }

            {editable &&
            <Button disabled>
                Confirm
            </Button>
            }
        </Segment>
    );
};

export default ConstructedCiv;