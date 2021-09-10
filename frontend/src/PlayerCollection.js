import {Button, Container, Header, Icon, Modal, Segment} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import ImpRandom from "./ImpRandom";
import CivCardGroup from "./cards/CivCardGroup";
import axios from "axios";
import PlayerAvatar from "./player/PlayerAvatar";
import {Link} from "react-router-dom";


const PlayerCollection = ({loggedInPlayer}) => {

    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(true);
    const [canMulligan, setCanMulligan] = useState(false);
    const [showMulliganModal, setShowMulliganModal] = useState(false);
    const [packsToOpen, setPacksToOpen] = useState([]);
    const [playerInfo, setPlayerInfo] = useState();

    useEffect(() => {
        axios.get("/api/player/collection")
            .then((response) => {
                setLoading(false);
                if (response.data) {
                    response.data.sort(ImpRandom.cardSort);
                    setCollection(response.data)
                }
            })
            .catch((error) => {
                console.error('Error retrieving player cards', error);
            })

        axios.get('/api/player')
            .then(response => {
                setPlayerInfo(response.data)
            })
            .catch(console.error);

        axios.get('/api/player/packs')
            .then(response => {
                setPacksToOpen(response.data)
            })
            .catch(console.error);

    }, []);

    useEffect(() => {
        axios.get("/api/player/mulligan")
            .then((response) => {
                setCanMulligan(response.data);
            })
            .catch((error) => {
                console.error('Error retrieving mulligan info', error);
            })
    }, [collection]);

    const confirmMulligan = () => {
        setLoading(true);
        axios.put("/api/player/mulligan")
            .then((response) => {
                if (response.data) {
                    response.data.sort(ImpRandom.cardSort);
                    setCollection(response.data);
                }
                setLoading(false);
            })
            .catch((error) => {
                console.error('Error retrieving collection after mulligan', error);
            })
        setShowMulliganModal(false);
    };

    return (
        <React.Fragment>

            <Container>

            {loading &&
            <div>Loading...</div>
            }

            {!loading &&
                <React.Fragment>
                    <Header as='h2'>
                        <PlayerAvatar size='large' player={loggedInPlayer} />
                        {loggedInPlayer.discordUsername}'s Collection
                    </Header>
                    {packsToOpen.length > 0 &&
                    <Button color='green' as={Link} to='/packs' style={{'marginBottom': '1em'}}>
                        Click here to open a card pack!
                    </Button>
                    }
                    {playerInfo &&
                    <Segment>
                        {playerInfo.balance > 1 &&
                        <Button as={Link} to='/pack-shop' floated='right'>
                            <Icon name='shop' />
                            Go to the pack shop!
                        </Button>
                        }
                        <strong>{playerInfo.balance}</strong> stars available to spend<br />
                        {playerInfo.totalPointsEarned} stars earned in total
                    </Segment>
                    }

                    {canMulligan &&
                    <Modal
                        basic
                        onClose={() => setShowMulliganModal(false)}
                        onOpen={() => setShowMulliganModal(true)}
                        open={showMulliganModal}
                        size='small'
                        trigger={<Button color='red' style={{'marginBottom': '1em'}}>Mulligan collection</Button>}
                    >
                        <Header icon>
                            <Icon name='redo' />
                            Reset entire collection
                        </Header>
                        <Modal.Content>
                            <p>
                                WARNING! This will reset your current collection and
                                <strong> give you ONE LESS CARD</strong> in the new collection.
                                Only do this if you're sure your current collection is horrible.
                            </p>
                        </Modal.Content>
                        <Modal.Actions>
                            <Button inverted onClick={() => setShowMulliganModal(false)}>
                                Cancel
                            </Button>
                            <Button basic color='red' inverted onClick={confirmMulligan}>
                                <Icon name='checkmark' /> Confirm mulligan
                            </Button>
                        </Modal.Actions>
                    </Modal>
                    }



                    <CivCardGroup cards={collection} cardClicked={() => {}} />
                </React.Fragment>
            }

            </Container>
        </React.Fragment>
    );
}

export default PlayerCollection;