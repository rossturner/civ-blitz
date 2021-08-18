import {useEffect, useState} from "react";
import {Button, Message, Modal} from "semantic-ui-react";
import axios from "axios";


const EditMatchModal = ({match, onMatchUpdated, onCancel}) => {

    const [showModal, setShowModal] = useState(false);
    const [errorText, setErrorText] = useState('');

    useEffect(() => {
        if (match.matchId) {
            setShowModal(true);
        }
    }, [match]);

    const onPrimaryClick = () => {
        axios.put('/api/matches/'+match.matchId+'/SIGNUPS', {})
            .then(response => {
                onMatchUpdated(response.data);
                setShowModal(false);
            })
            .catch((error) => {
                setErrorText(error.response.data.message);
            })
    };

    return (
        <Modal
            onClose={() => setShowModal(false)}
            open={showModal}
        >
            <Modal.Header>Revert to Signups phase?</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <p>This will <strong>revert</strong> {match.matchName} to the signups phase - all decks will be cancelled and cards returned to players.</p>
                    <p>Please be sure you wish to proceed.</p>
                </Modal.Description>

                {errorText &&
                <Message error>
                    {errorText}
                </Message>
                }
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={() => {setShowModal(false); onCancel();}}>Cancel</Button>
                <Button onClick={onPrimaryClick} negative>Revert to signups</Button>
            </Modal.Actions>
        </Modal>
    );
};


export default EditMatchModal;