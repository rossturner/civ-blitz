import {Button, List} from "semantic-ui-react";
import {matchStateToString} from "./MatchState";
import React, {useState} from "react";
import EditMatchModal from "./EditMatchModal";
import ProceedToDraftModal from "./ProceedToDraftModal";
import axios from "axios";
import RevertToSignupsModal from "./RevertToSignupsModal";


const MatchHeader = ({match, loggedInPlayer, onMatchUpdated}) => {

    const userIsAdmin = loggedInPlayer && loggedInPlayer.isAdmin;
    const [editingMatch, setEditingMatch] = useState({});
    const [matchToProceedToDraft, setMatchToProceedToDraft] = useState({});
    const [matchToRevertToSignups, setMatchToRevertToSignups] = useState({});

    const canSignUp = match.matchState === 'SIGNUPS' && loggedInPlayer && !match.signups.some(signup => signup.playerId === loggedInPlayer.discordId);
    const canResign = match.matchState === 'SIGNUPS' && loggedInPlayer && match.signups.some(signup => signup.playerId === loggedInPlayer.discordId);

    const matchUpdated = () => {
        setEditingMatch({});
        setMatchToProceedToDraft({});
        setMatchToRevertToSignups({});
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
                    <Button negative onClick={() => setMatchToRevertToSignups(match)} floated='right'
                            content='Revert to Signups' size='tiny' icon='step backward'/>
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
            <ProceedToDraftModal match={matchToProceedToDraft} onMatchUpdated={matchUpdated}/>
            <RevertToSignupsModal match={matchToRevertToSignups} onMatchUpdated={matchUpdated}/>

        </React.Fragment>
    );
}

export default MatchHeader;