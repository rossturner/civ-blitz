import {Button, List} from "semantic-ui-react";
import {matchStateToString} from "./MatchState";
import React, {useState} from "react";
import EditMatchModal from "./EditMatchModal";
import ProceedToDraftModal from "./ProceedToDraftModal";
import axios from "axios";
import RevertMatchStateModal from "./RevertMatchStateModal";


const MatchHeader = ({match, loggedInPlayer, onMatchUpdated}) => {

    const userIsAdmin = loggedInPlayer && loggedInPlayer.isAdmin;
    const [editingMatch, setEditingMatch] = useState({});
    const [matchToProceedToDraft, setMatchToProceedToDraft] = useState({});
    const [revertToState, setRevertToState] = useState();

    const canSignUp = match.matchState === 'SIGNUPS' && loggedInPlayer && !match.signups.some(signup => signup.playerId === loggedInPlayer.discordId);
    const canResign = match.matchState === 'SIGNUPS' && loggedInPlayer && match.signups.some(signup => signup.playerId === loggedInPlayer.discordId);

    const matchUpdated = () => {
        setEditingMatch({});
        setRevertToState(undefined);
        setMatchToProceedToDraft({});
        onMatchUpdated();
    }

    const onSignup = () => {
        axios.post('/api/matches/' + match.matchId + '/players')
            .then(() => {
                onMatchUpdated();
            })
            .catch(console.error)
    }
    const onResign = () => {
        axios.delete('/api/matches/' + match.matchId + '/players')
            .then(() => {
                onMatchUpdated();
            })
            .catch(console.error)
    }

    return (
        <React.Fragment>
            {userIsAdmin &&
            <List floated='right'>
                {match.matchState === 'SIGNUPS' &&
                <List.Item>
                    <Button primary onClick={() => setMatchToProceedToDraft(match)} floated='right'
                            content='Proceed to Draft' size='tiny' icon='step forward'/>
                </List.Item>
                }

                {match.matchState === 'DRAFT' &&
                <List.Item>
                    <Button negative onClick={() => setRevertToState('SIGNUPS')} floated='right'
                            content='Revert to Signups' size='tiny' icon='step backward'/>
                </List.Item>
                }

                {match.matchState === 'POST_MATCH' &&
                <List.Item>
                    <Button negative onClick={() => setRevertToState('IN_PROGRESS')} floated='right'
                            content='Revert to in progress' size='tiny' icon='step backward'/>
                </List.Item>
                }

                <List.Item>
                    <Button onClick={() => setEditingMatch(match)} floated='right' content='Edit' size='tiny'
                            icon='edit'/>
                </List.Item>
            </List>
            }
            <p>Status: <strong>{matchStateToString(match.matchState)}</strong></p>
            <p>Timeslot: {match.timeslot}</p>
            <p>Players: {match.signups.length}</p>
            {canSignUp &&
            <Button primary onClick={onSignup}>Sign up to this match</Button>
            }
            {canResign &&
            <Button negative onClick={onResign}>Resign from this match</Button>
            }

            <EditMatchModal match={editingMatch} onMatchUpdated={matchUpdated}/>
            <ProceedToDraftModal match={matchToProceedToDraft} onMatchUpdated={matchUpdated} onCancel={() => setMatchToProceedToDraft({})}/>
            <RevertMatchStateModal match={match} targetState={revertToState} onMatchUpdated={matchUpdated} onCancel={() => setRevertToState(undefined)}/>

        </React.Fragment>
    );
}

export default MatchHeader;