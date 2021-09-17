import {Button, Header, Icon, Input, Table} from "semantic-ui-react";
import React from 'react';
import PlayerAvatar from "../player/PlayerAvatar";
import axios from "axios";
import {useHistory} from "react-router-dom";


const MatchLeaderboard = ({match, leaderboard, loggedInPlayer, leaderboardChanged}) => {

    const tableRows = [];
    const currentPlayerSignup = match.signups && match.signups.find(s => s.playerId === loggedInPlayer.discordId);
    const playerIsAdminNotInMatch = true;//!currentPlayerSignup && loggedInPlayer.isAdmin;
    const canBeCompleted = match.matchState === 'POST_MATCH' && playerIsAdminNotInMatch;

    const updateScore = (playerId, score) => {
        const newLeaderboard = {...leaderboard}
        newLeaderboard[playerId] = Number(score);
        leaderboardChanged(newLeaderboard);
    }

    for (const [playerId, score] of Object.entries(leaderboard)) {
        const signup = match.signups.find(signup => signup.playerId === playerId);
        const stars = [];
        for (let starNum = 0; starNum < score; starNum++) {
            stars.push(<Icon size='large' color='yellow' name='star' />);
        }

        tableRows.push(<Table.Row key={playerId}>
            <Table.Cell>
                <Header as='h4' image>
                    <PlayerAvatar player={signup.player} />
                    <Header.Content>
                        {signup.player.discordUsername}
                    </Header.Content>
                </Header>
            </Table.Cell>
            <Table.Cell>{stars}</Table.Cell>
            {canBeCompleted &&
            <Table.Cell>
                <Input type='number' value={score} onChange={(event, data) => updateScore(playerId, data.value)} />
            </Table.Cell>
            }
        </Table.Row>);
    }


    const history = useHistory();

    const completeMatch = () => {
        axios.put('/api/matches/'+match.matchId+'/COMPLETED', leaderboard)
            .then(response => {
                history.push("/matches");
            })
            .catch(console.error)
    }

    return (
        <React.Fragment>
            <Table basic='very' celled collapsing>
                {canBeCompleted &&
                <Table.Header>
                    <Table.HeaderCell>Player</Table.HeaderCell>
                    <Table.HeaderCell>Stars claimed from objectives</Table.HeaderCell>
                    <Table.HeaderCell>Confirm final score</Table.HeaderCell>
                </Table.Header>
                }
                <Table.Body>
                    {tableRows}
                </Table.Body>
            </Table>
            {canBeCompleted &&
            <Button color='green' onClick={completeMatch}>Finalise match and award stars</Button>
            }
        </React.Fragment>
    );
};

export default MatchLeaderboard;