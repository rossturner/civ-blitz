import {Button, Card, Container, Header, Segment, Select} from "semantic-ui-react";
import React, {useState} from "react";
import CivCard from "./cards/CivCard";
import {MAIN_CATEGORIES} from "./cards/CardStore";

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
            searchParams.append('cardIdentifier', card.identifier);
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

    const hasSelectedNecessaryCards = MAIN_CATEGORIES.every(requiredCategory => cards.find(card => card.cardCategory === requiredCategory));

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
                <Button primary disabled={!hasSelectedNecessaryCards} onClick={() => onConfirmClick()}>
                    Confirm
                </Button>
                }

                {(!editable || (alwaysEditing && hasSelectedNecessaryCards)) &&
                <Button as='a' href={downloadLink} disabled={!selectedBias}>
                    Download mod
                </Button>
                }
            </Container>
        </Segment>
    );
};

export default ConstructedCiv;