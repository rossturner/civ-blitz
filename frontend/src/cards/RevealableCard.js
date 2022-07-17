import React, {useState} from "react";
import {Card, Image, Transition} from "semantic-ui-react";
import CivCard from "./CivCard";
import './CivCard.css'

const RevealableCard = ({cardJson}) => {

    const [placeholderDisplayed, setPlaceholderDisplayed] = useState(true);
    const [revealed, setRevealed] = useState(false);

    const placeholderClicked = () => {
        if (placeholderDisplayed) {
            setPlaceholderDisplayed(false);
        }
    };

    const placeholderComplete = () => {
        setRevealed(true);
    };

    return (
        <React.Fragment>
            {!revealed &&
            <Transition visible={placeholderDisplayed} animation='scale' duration={500}
                        onComplete={placeholderComplete}>
                <Card className='civ-card' onClick={placeholderClicked}>
                    <Image src={'/images/CivBlitz_'+cardJson.rarity+'.png'} wrapped ui={false} />
                    <Card.Content>
                        <Card.Header>???</Card.Header>
                        <Card.Description>
                            Click this card to reveal it!
                        </Card.Description>
                    </Card.Content>
                </Card>
            </Transition>
            }

            <Transition visible={revealed} animation='scale' duration={500}>
                 <CivCard cardJson={cardJson} clickDisabled={true} />
             </Transition>
        </React.Fragment>
    );
};

export default RevealableCard;