import {useEffect, useState} from "react";
import {Button, Input, Modal} from "semantic-ui-react";
import axios from "axios";


const EditMatchModal = ({editingMatch, onMatchUpdated}) => {

    const [showEditModal, setShowEditModal] = useState(false);
    const [matchName, setMatchName] = useState('');
    const [matchTimeslot, setMatchTimeslot] = useState('');

    useEffect(() => {
        if (editingMatch.matchId) {
            setMatchName(editingMatch.matchName);
            setMatchTimeslot(editingMatch.timeslot);
            setShowEditModal(true);
        }
    }, [editingMatch]);

    const save = () => {
        axios.post('/api/matches/'+editingMatch.matchId, {matchTimeslot, matchName})
            .then(response => {
                onMatchUpdated(response.data);
                setShowEditModal(false);
            })
            .catch(console.error);
    };

    return (
        <Modal
            onClose={() => setShowEditModal(false)}
            open={showEditModal}
        >
            <Modal.Header>Editing match {editingMatch.matchName}</Modal.Header>
            <Modal.Content image>
                <Modal.Description>
                    <Input label='Name' value={matchName} onChange={(event, data) => setMatchName(data.value)} fluid />
                    <Input label='Timeslot' value={matchTimeslot} onChange={(event, data) => setMatchTimeslot(data.value)} fluid />
                </Modal.Description>
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={() => setShowEditModal(false)}>
                    Cancel
                </Button>
                <Button
                    content='Save changes'
                    onClick={save}
                    primary
                />
            </Modal.Actions>
        </Modal>
    );
};


export default EditMatchModal;