import {useEffect, useState} from "react";
import {Button, Message, Modal, Select} from "semantic-ui-react";
import axios from "axios";

const AdminUnclaimObjectiveModal = ({match, open, publicObjectives, secretObjectives, onConfirm, onCancel}) => {

    const [showModal, setShowModal] = useState(false);
    const [errorText, setErrorText] = useState('');

    const [selectedPlayer, setSelectedPlayer] = useState();
    const [selectedObjective, setSelectedObjective] = useState();


    useEffect(() => {
        if (open) {
            setShowModal(true);
        } else {
            setShowModal(false);
            setSelectedPlayer(undefined);
            setSelectedObjective(undefined);
        }
    }, [open]);

    const onProceed = () => {
        axios.delete('/api/admin/'+selectedPlayer+'/matches/'+match.matchId+'/objectives/'+selectedObjective)
            .then(onConfirm)
            .catch((error) => {
                setErrorText(error.response.data.message);
            })
    };

    const playerOptions = match.signups.map(signup => {
        return {
            key: signup.player.playerId,
            value: signup.player.playerId,
            text: signup.player.discordUsername
        }
    });
    let objectiveOptions = [];
    if (selectedPlayer) {
        objectiveOptions = objectiveOptions.concat(publicObjectives.filter(
            pub => pub.claimedByPlayerIds.includes(selectedPlayer)
        ).map(pub => {
            return {
                key: pub.enumName,
                value: pub.enumName,
                text: pub.objectiveName
            }
        }))

        objectiveOptions = objectiveOptions.concat(secretObjectives.filter(
            secret => secret.playerId === selectedPlayer && secret.claimed && secret.selected
        ).map(secret => {
            return {
                key: secret.objective,
                value: secret.objective,
                text: secret.objectiveName + ' (Secret)'
            };
        }));
    }

    return (
        <Modal
            onClose={onCancel}
            open={showModal}
        >
            <Modal.Header>Admin unclaim objective</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <Select placeholder='Select player' options={playerOptions} onChange={(event, {value}) => setSelectedPlayer(value)} />
                    <Select placeholder='Select objective' options={objectiveOptions} onChange={(event, {value}) => setSelectedObjective(value)} />
                </Modal.Description>

                {errorText &&
                <Message error>
                    {errorText}
                </Message>
                }
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={onCancel}>Cancel</Button>
                <Button onClick={onProceed} negative disabled={!selectedObjective}>Unclaim objective</Button>
            </Modal.Actions>
        </Modal>
    );
};

export default AdminUnclaimObjectiveModal;