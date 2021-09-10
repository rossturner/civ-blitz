import {Button, Card, Container, Header, Segment, Select} from "semantic-ui-react";
import React, {useState} from "react";
import CivCard from "./cards/CivCard";

const ConstructedCiv = ({index, cards, editable, onCardClick, onConfirmClick, alwaysEditing}) => {

    const cardItems = cards.map((card, index) => {
        return <CivCard key={index} cardJson={card} onClick={() => editable ? onCardClick(card) : {}}/>;
    });

    let downloadLink = "/api/mods?";

    const [selectedBias, setSelectedBias] = useState();

    const searchParams = new URLSearchParams();
    const startBiases = {};
    if (cards) {
        cards.forEach(card => {
            searchParams.append('traitType', card.traitType);
            startBiases[card.civilizationType] = card.civilizationFriendlyName;
        });
    }
    if (setSelectedBias) {
        searchParams.append('startBias', selectedBias);
    }
    downloadLink += searchParams.toString();

    const options = [];
    for (const [civType, friendlyName] of Object.entries(startBiases)) {
        options.push({
            key: civType,
            value: civType,
            text: friendlyName
        });
    }

    return (
        <Segment>
            <Header as='h3'>Civ {index}</Header>

            {cards.length === 0 &&
            <p>Add some cards to get started</p>
            }

            <Card.Group>
                {cardItems}
            </Card.Group>


            <Container style={{'paddingTop': '2em'}}>
                {cards.length > 0 &&
                <div style={{'paddingBottom': '1em'}}>
                    <Select placeholder='Select start bias...' options={options} onChange={(event, {value}) => setSelectedBias(value)}/>
                </div>
                }

                {editable && !alwaysEditing &&
                <Button primary disabled={cards.length < 4} onClick={() => onConfirmClick()}>
                    Confirm
                </Button>
                }

                {(!editable || (alwaysEditing && cards.length === 4)) &&
                <Button as='a' href={downloadLink} disabled={!selectedBias}>
                    Download mod
                </Button>
                }
            </Container>
        </Segment>
    );
};

export default ConstructedCiv;