import {Container, Header, Table} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import PlayerAvatar from "./PlayerAvatar";

const PlayerLeaderboard = () => {

    const [players, setPlayers] = useState([]);

    useEffect(() => {
        axios.get('/api/player/leaderboard')
            .then(response => {
                setPlayers(response.data);
            })
            .catch(console.error)
    }, []);

    const playerRows = players.map((player, index) => <Table.Row key={player.discordId}>
        <Table.Cell>#{index+1}</Table.Cell>
        <Table.Cell><PlayerAvatar size='mini' player={player}/> {player.discordUsername}</Table.Cell>
        <Table.Cell>{Number(player.rankingScore).toFixed(1)}</Table.Cell>
        <Table.Cell>{player.totalPointsEarned}</Table.Cell>
    </Table.Row>);

    return (
        <React.Fragment>
            <Container>
                <Header as='h2'>Leaderboard</Header>

                <Table celled>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell>Ranking</Table.HeaderCell>
                            <Table.HeaderCell>Player</Table.HeaderCell>
                            <Table.HeaderCell>Ranking score</Table.HeaderCell>
                            <Table.HeaderCell>Total stars earned</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {playerRows}
                    </Table.Body>
                </Table>
            </Container>
        </React.Fragment>
    );
}

export default PlayerLeaderboard;