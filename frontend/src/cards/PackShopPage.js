import {Card, Container, Header, Segment} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import SingleCardPurchase from "./shop/SingleCardPurchase";
import TripleCardPurchase from "./shop/TripleCardPurchase";
import BoosterPackPurchase from "./shop/BoosterPackPurchase";


const PackShopPage = ({loggedInPlayer}) => {

    const [playerInfo, setPlayerInfo] = useState();

    useEffect(() => {
        axios.get('/api/player')
            .then(response => {
                setPlayerInfo(response.data)
            })
            .catch(console.error);
    }, []);

    return (
        <Container>
            <Header as='h2'>Pack shop</Header>

            {!loggedInPlayer &&
            <p>You must be logged in to view this page</p>
            }

            {loggedInPlayer &&
            <React.Fragment>
                {playerInfo &&
                <Segment>
                    <strong>{playerInfo.balance}</strong> stars available to spend<br />
                    {playerInfo.totalPointsEarned} stars earned in total
                </Segment>
                }

                <Card.Group>

                    <SingleCardPurchase />
                    <TripleCardPurchase />
                    <BoosterPackPurchase />

                </Card.Group>

            </React.Fragment>
            }

        </Container>
    );
}

export default PackShopPage;