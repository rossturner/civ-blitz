import {Button, Container, Header, List, Loader, Segment} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import PlayerAvatar from "../player/PlayerAvatar";


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

    const matchSections = matchList.map(match => {
        const playerSections = match.signups.map(signup => {
            return (<List.Item key={signup.playerId}>
                <PlayerAvatar player={signup.player} size='mini'/>
                <List.Content>
                    <List.Header>{signup.player.discordUsername}</List.Header>
                </List.Content>
            </List.Item>)
        });

        const canSignUp = match.matchState === 'SIGNUPS' && loggedInPlayer && !match.signups.some(signup => signup.playerId === loggedInPlayer.discordId);
        const canResign = match.matchState === 'SIGNUPS' && loggedInPlayer && match.signups.some(signup => signup.playerId === loggedInPlayer.discordId);

        const onSignup = () => {
            axios.put('/api/matches/'+match.matchId+'/players')
                .then(() => {
                    setLoading(true)
                })
                .catch(console.error)
        }
        const onResign = () => {
            axios.delete('/api/matches/'+match.matchId+'/players')
                .then(() => {
                    setLoading(true)
                })
                .catch(console.error)
        }

        return <Segment key={match.matchId}>
            <Header as='h3'>{match.matchName}</Header>
            <p>Status: {match.matchState}</p>
            <p>Timeslot: {match.timeslot}</p>
            <p>Players:</p>
            {canSignUp &&
            <Button primary onClick={onSignup}>Sign up to this match</Button>
            }
            {canResign &&
            <Button negative onClick={onResign}>Resign from this match</Button>
            }

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
                <Loader />
                }

                {!loading &&
                <React.Fragment>
                    {matchSections}

                    {matchList.length === 0 &&
                    <p>Civ Imperium is still under development, check back later!</p>
                    }
                </React.Fragment>
                }
            </Container>
        </React.Fragment>
    );
};


export default MatchesPage;