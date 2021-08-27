import {useEffect, useState} from "react";
import {Button, Checkbox, List, Message, Modal} from "semantic-ui-react";
import axios from "axios";
import PlayerAvatar from "../player/PlayerAvatar";


const ProceedToDraftModal = ({match, onMatchUpdated, onCancel}) => {

    const [showModal, setShowModal] = useState(false);
    const [selectedPlayerIds, setSelectedPlayerIds] = useState([]);
    const [errorText, setErrorText] = useState('');

    useEffect(() => {
        if (match.matchId) {
            setShowModal(true);
            setSelectedPlayerIds(match.signups.map(signup => signup.playerId));
        }
    }, [match]);

    const onProceed = () => {
        axios.put('/api/matches/'+match.matchId+'/DRAFT', {playerIds: selectedPlayerIds})
            .then(response => {
                onMatchUpdated(response.data);
                setShowModal(false);
            })
            .catch((error) => {
                setErrorText(error.response.data.message);
            })
    };

    const togglePlayerSelection = (signup) => {
        const newList = [].concat(selectedPlayerIds);
        if (selectedPlayerIds.includes(signup.playerId)) {
            setSelectedPlayerIds(newList.filter(id => id !== signup.playerId));
        } else {
            newList.push(signup.playerId);
            setSelectedPlayerIds(newList);
        }
    }

    const playerItems = match.signups && match.signups.map(signup => {
        return (
            <List.Item key={signup.playerId} onClick={() => {togglePlayerSelection(signup)}}>
                <Checkbox checked={selectedPlayerIds.includes(signup.playerId)} />
                <PlayerAvatar player={signup.player} size='mini'/>
                <List.Content>
                    <List.Header>{signup.player.discordUsername}</List.Header>
                </List.Content>
            </List.Item>
        );
    });

    return (
        <Modal
            onClose={() => setShowModal(false)}
            open={showModal}
        >
            <Modal.Header>Proceed to Draft phase?</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <p>This will advance {match.matchName} to the draft phase - no more players will be able to join.</p>
                    <p>Please confirm the final player list below:</p>
                    <List selection verticalAlign='middle'>
                        {playerItems}
                    </List>
                </Modal.Description>

                {errorText &&
                <Message error>
                    {errorText}
                </Message>
                }
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={() => {setShowModal(false); onCancel()}}>Cancel</Button>
                <Button onClick={onProceed} primary disabled={selectedPlayerIds.length < 2}>Proceed to Draft</Button>
            </Modal.Actions>
        </Modal>
    );
};


export default ProceedToDraftModal;