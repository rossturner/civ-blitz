import React, {useState} from "react";
import {Card, Image, Transition} from "semantic-ui-react";
import ImperiumCard from "./ImperiumCard";
import './ImperiumCard.css'

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
                <Card className='imperium-card' onClick={placeholderClicked}>
                    <Image src='/images/imperium.png' wrapped ui={false} />
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
                 <ImperiumCard cardJson={cardJson} clickDisabled={true} />
             </Transition>
        </React.Fragment>
    );
};

export default RevealableCard;