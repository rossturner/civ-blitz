import {Container, Header, List, Loader, Segment} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import PlayerAvatar from "../player/PlayerAvatar";
import {Link} from "react-router-dom";
import MatchHeader from "./MatchHeader";


const MatchesPage = ({loggedInPlayer}) => {

    const [loading, setLoading] = useState(true);
    const [matchList, setMatchList] = useState([]);

    useEffect(() => {
        axios.get('/api/matches')
            .then((response) => {
                setMatchList(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Error retrieving matches', error);
            })
    }, [loading]);

    const onMatchUpdated = () => {
        setLoading(true);
    }

    const matchSections = matchList.map(match => {
        const playerSections = match.signups.map(signup => {
            return (<List.Item key={signup.playerId}>
                <PlayerAvatar player={signup.player} size='mini'/>
                <List.Content>
                    <List.Header>{signup.player.discordUsername}</List.Header>
                </List.Content>
            </List.Item>)
        });

        return <Segment key={match.matchId}>
            <Header as='h3'><Link to={'/matches/'+match.matchId}>{match.matchName}</Link></Header>

            <MatchHeader match={match} loggedInPlayer={loggedInPlayer} onMatchUpdated={onMatchUpdated} />

            {match.signups &&
            <List horizontal>
                {playerSections}
            </List>
            }
        </Segment>
    })

    return (
        <React.Fragment>
            <Container style={{marginTop: '6em'}}>
                <Header as='h2'>Matches</Header>

                {loading &&
                <Loader/>
                }

                {!loading &&
                <React.Fragment>
                    {matchSections}

                    {matchList.length === 0 &&
                    <p>No matches are scheduled at this time, go and ask an Imperium admin to set one up</p>
                    }
                </React.Fragment>
                }
            </Container>
        </React.Fragment>
    );
};


export default MatchesPage;