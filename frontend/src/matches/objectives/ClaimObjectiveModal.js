import {useEffect, useState} from "react";
import {Button, Message, Modal} from "semantic-ui-react";
import axios from "axios";
import ObjectiveCard from "./ObjectiveCard";

const ClaimObjectiveModal = ({match, objective, onConfirm, onCancel}) => {

    const [showModal, setShowModal] = useState(false);
    const [errorText, setErrorText] = useState('');

    useEffect(() => {
        if (objective.enumName) {
            setShowModal(true);
        } else {
            setShowModal(false);
            setErrorText('');
        }
    }, [objective]);

    const onProceed = () => {
        axios.post('/api/matches/'+match.matchId+'/objectives/'+objective.enumName)
            .then(onConfirm)
            .catch((error) => {
                setErrorText(error.response.data.message);
            })
    };

    return (
        <Modal
            onClose={() => setShowModal(false)}
            open={showModal}
        >
            <Modal.Header>Are you sure you wish to claim this objective?</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <ObjectiveCard objectiveJson={objective} clickDisabled={true} />
                    <p>You <strong>must</strong> notify the other players in the game as you claim this objective.</p>
                    <p>If there is no spectator, please provide proof using a screenshot to the Discord channel. Win-key+Shift+S is your friend.</p>
                </Modal.Description>

                {errorText &&
                <Message error>
                    {errorText}
                </Message>
                }
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={() => {setShowModal(false); onCancel()}}>Cancel</Button>
                <Button onClick={onProceed} primary>Confirm</Button>
            </Modal.Actions>
        </Modal>
    );
};


export default ClaimObjectiveModal;