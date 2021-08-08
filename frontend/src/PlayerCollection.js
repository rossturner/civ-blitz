import {Button, Container, Header, Icon, Modal} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import ImpRandom from "./ImpRandom";
import ImperiumCardGroup from "./cards/ImperiumCardGroup";
import axios from "axios";


const PlayerCollection = ({loggedInPlayer}) => {

    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(true);
    const [canMulligan, setCanMulligan] = useState(false);
    const [showMulliganModal, setShowMulliganModal] = useState(false);

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

            <Container style={{marginTop: '6em'}}>

            {loading &&
            <div>Loading...</div>
            }

            {!loading &&
                <React.Fragment>
                    <Header as='h2'>{loggedInPlayer.discordUsername}'s Collection</Header>

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



                    <ImperiumCardGroup cards={collection} cardClicked={() => {}} />
                </React.Fragment>
            }

            </Container>
        </React.Fragment>
    );
}

export default PlayerCollection;