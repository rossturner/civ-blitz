import {Button, CardGroup, Container, Header} from "semantic-ui-react";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import axios from "axios";
import RevealableCard from "./RevealableCard";


const PackOpener = () => {

    const [loading, setLoading] = useState(true);
    const [packContents, setPackContents] = useState([]);

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
        <Container style={{marginTop: '6em'}}>
            <Header as='h2'>Match Booster Pack</Header>

            {loading &&
            <p>Loading...</p>
            }

            {!loading &&
            <React.Fragment>
                {packContents.length > 0 &&
                <React.Fragment>
                    <p>You earned this pack due to playing in a match. Note that free use cards do not grant a replacement card
                        to that category.</p>

                    <CardGroup centered>
                        {packContents.map(card => <RevealableCard key={card.cardName} cardJson={card} />)}
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