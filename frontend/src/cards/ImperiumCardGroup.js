import ImperiumCard from "./ImperiumCard";
import {Card, Checkbox, Container} from "semantic-ui-react";
import React, {useState} from "react";


const ImperiumCardGroup = ({cards, cardClicked}) => {

    const [currentFilter, setCurrentFilter] = useState('');

    const filters = {
        'All': '',
        'Civilization Ability': 'CivilizationAbility',
        'Leader Ability': 'LeaderAbility',
        'UniqueInfrastructure': 'UniqueInfrastructure',
        'UniqueUnit': 'UniqueUnit'
    };
    const checkboxes = [];
    for (const [displayText, value] of Object.entries(filters)) {
        checkboxes.push(
            <Checkbox
                key={value}
                radio
                label={displayText}
                name='filterRadioGroup'
                value={value}
                checked={currentFilter === value}
                onChange={(event, checkbox) => setCurrentFilter(checkbox.value)}
                style={{'paddingRight': '2em'}}
            />
        );
    }


    const cardItems = cards.map((cardJson, index) => {
        if (currentFilter && currentFilter !== cardJson.cardCategory) {
            return null;
        } else {
            return (<ImperiumCard key={index} cardJson={cardJson} onClick={() => cardClicked(cardJson)}/>);
        }
    })

    return (
        <React.Fragment>
            <Container style={{'paddingBottom': '1em'}}>
                {checkboxes}
            </Container>

            <Card.Group>
                {cardItems}
            </Card.Group>
        </React.Fragment>
    );
};

export default ImperiumCardGroup;