import {useEffect, useState} from "react";
import {Button, Message, Modal} from "semantic-ui-react";
import axios from "axios";
import {matchStateToString} from "./MatchState";


const RevertMatchStateModal = ({match, targetState, onMatchUpdated, onCancel}) => {

    const [showModal, setShowModal] = useState(false);
    const [errorText, setErrorText] = useState('');

    useEffect(() => {
        if (targetState) {
            setShowModal(true);
        }
    }, [targetState]);

    const onPrimaryClick = () => {
        axios.put('/api/matches/'+match.matchId+'/'+targetState, {})
            .then(response => {
                onMatchUpdated(response.data);
                setShowModal(false);
            })
            .catch((error) => {
                setErrorText(error.response.data.message);
            })
    };

    const directionVerb = targetState === 'POST_MATCH' ? 'Proceed' : 'Revert';

    return (
        <Modal
            onClose={() => setShowModal(false)}
            open={showModal}
        >
            <Modal.Header>{directionVerb} to {matchStateToString(targetState)} phase?</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    {targetState === 'IN_PROGRESS' &&
                    <p>This will <strong>revert</strong> <i>{match.matchName}</i> to the in-progress phase - this is intended for use
                        when someone has claimed an objective incorrectly and this needs to changed.</p>
                    }
                    {targetState === 'SIGNUPS' &&
                    <p>This will <strong>revert</strong> <i>{match.matchName}</i> to the signups phase - all decks will be cancelled and cards returned to players.</p>
                    }
                    {targetState === 'POST_MATCH' &&
                    <p>This will advance <i>{match.matchName}</i> to the post-match phase - no more objectives can be claimed after this point.</p>
                    }
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
                <Button onClick={onPrimaryClick} primary={directionVerb === 'Proceed'} negative={directionVerb !== 'Proceed'}>
                    {directionVerb} to {matchStateToString(targetState)}
                </Button>
            </Modal.Actions>
        </Modal>
    );
};


export default RevertMatchStateModal;