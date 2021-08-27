import {Header, Icon, Table} from "semantic-ui-react";
import PlayerAvatar from "../player/PlayerAvatar";


const MatchLeaderboard = ({match, leaderboard}) => {

    const tableRows = [];
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
        </Table.Row>);
    }

    return (
        <Table basic='very' celled collapsing>
            <Table.Body>
                {tableRows}
            </Table.Body>
        </Table>
    );
};

export default MatchLeaderboard;