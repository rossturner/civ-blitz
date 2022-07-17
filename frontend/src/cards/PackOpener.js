import {Button, CardGroup, Container, Header} from "semantic-ui-react";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import axios from "axios";
import RevealableCard from "./RevealableCard";


const PackOpener = () => {

    const [loading, setLoading] = useState(true);
    const [packContents, setPackContents] = useState();

    useEffect(() => {
        axios.post('/api/player/packs')
            .then(response => {
                setPackContents(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error(error);
                setLoading(false);
            });
    }, []);

    return (
        <Container>

            {loading &&
            <p>Loading...</p>
            }

            {!loading &&
            <React.Fragment>
                {packContents &&
                <React.Fragment>
                    <Header as='h2'>
                        {packContents.packType === 'SINGLE_CARD' && 'Single Card Pack'}
                        {packContents.packType === 'MULTIPLE_CARDS' && 'Multiple Card Pack'}
                        {packContents.packType === 'MATCH_BOOSTER' && 'Match Booster Pack'}
                    </Header>
                    {packContents.packType === 'MATCH_BOOSTER' && packContents.cards.length < 4 &&
                    <p>You earned this pack due to playing in a match. Note that free use cards do not grant a
                        replacement card to that category.</p>
                    }

                    <CardGroup centered>
                        {packContents.cards.map(card => <RevealableCard key={card.identifier} cardJson={card} />)}
                    </CardGroup>

                </React.Fragment>
                }


                {packContents.length === 0 &&
                <p>There was an error opening another pack, you probably refreshed the page, but don't worry,
                    the cards are in your collection.</p>
                }

                <Button as={Link} to='/collection' style={{marginTop: '1em'}}>Back to collection</Button>
            </React.Fragment>
            }

        </Container>

    );
};

export default PackOpener;