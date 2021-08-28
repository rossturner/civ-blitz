import {useEffect, useState} from "react";
import {Button, Modal} from "semantic-ui-react";
import axios from "axios";


const EditMatchModal = ({match, onCancel, onMatchDeleted}) => {

    const [showEditModal, setShowEditModal] = useState(false);

    useEffect(() => {
        if (match.matchId) {
            setShowEditModal(true);
        } else {
            setShowEditModal(false);
        }
    }, [match]);

    const confirmDelete = () => {
        axios.delete('/api/matches/'+match.matchId)
            .then(response => {
                setShowEditModal(false);
                onMatchDeleted();
            })
            .catch(console.error);
    };

    return (
        <Modal
            onClose={onCancel}
            open={showEditModal}
        >
            <Modal.Header>Delete match {match.matchName}?</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <p>Are you sure you wish to delete <i>{match.matchName}</i>?</p>
                </Modal.Description>
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={onCancel}>Cancel</Button>
                <Button onClick={confirmDelete} negative>Delete</Button>
            </Modal.Actions>
        </Modal>
    );
};


export default EditMatchModal;