import {useEffect, useState} from "react";
import {Button, Input, Modal} from "semantic-ui-react";
import axios from "axios";


const EditMatchModal = ({match, onMatchDeleted}) => {

    const [showEditModal, setShowEditModal] = useState(false);
    const [matchName, setMatchName] = useState('');
    const [matchTimeslot, setMatchTimeslot] = useState('');

    useEffect(() => {
        if (match.matchId) {
            setMatchName(match.matchName);
            setMatchTimeslot(match.timeslot);
            setShowEditModal(true);
        }
    }, [match]);

    const save = () => {
        axios.post('/api/matches/'+match.matchId, {matchTimeslot, matchName})
            .then(response => {
                onMatchDeleted(response.data);
                setShowEditModal(false);
            })
            .catch(console.error);
    };

    return (
        <Modal
            onClose={() => setShowEditModal(false)}
            open={showEditModal}
        >
            <Modal.Header>Editing match {match.matchName}</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <Input label='Name' value={matchName} onChange={(event, data) => setMatchName(data.value)} fluid />
                    <Input label='Timeslot' value={matchTimeslot} onChange={(event, data) => setMatchTimeslot(data.value)} fluid />
                </Modal.Description>
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={() => setShowEditModal(false)}> Cancel </Button>
                <Button content='Save changes' onClick={save} primary />
            </Modal.Actions>
        </Modal>
    );
};


export default EditMatchModal;