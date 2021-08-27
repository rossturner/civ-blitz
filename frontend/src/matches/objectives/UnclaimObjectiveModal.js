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
        }
    }, [objective]);

    const onProceed = () => {
        axios.delete('/api/matches/'+match.matchId+'/objectives/'+objective.enumName)
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
            <Modal.Header>Do you wish to no longer claim this objective?</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <ObjectiveCard objectiveJson={objective} clickDisabled={true} />
                    <p>This is in case you claimed the objective by mistake.</p>
                </Modal.Description>

                {errorText &&
                <Message error>
                    {errorText}
                </Message>
                }
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={() => {setShowModal(false); onCancel()}}>Cancel</Button>
                <Button onClick={onProceed} negative>Remove claim</Button>
            </Modal.Actions>
        </Modal>
    );
};


export default ClaimObjectiveModal;