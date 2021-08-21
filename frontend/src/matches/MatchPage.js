import {CardGroup, Container, Header, List, Loader, Segment} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import PlayerAvatar from "../player/PlayerAvatar";
import {useParams} from "react-router-dom";
import MatchHeader from "./MatchHeader";
import MatchCivBuilder from "./MatchCivBuilder";
import MapSettings from "./MapSettings";
import MatchCivViewer from "./MatchCivViewer";
import DownloadMatchModButton from "./DownloadMatchModButton";
import ObjectiveCard from "./objectives/ObjectiveCard";

const MatchPage = ({loggedInPlayer}) => {

    const {matchId} = useParams();
    const [loading, setLoading] = useState(true);
    const [match, setMatch] = useState({});
    const [publicObjectives, setPublicObjectives] = useState([]);

    const currentPlayerSignup = match.signups && match.signups.find(s => s.playerId === loggedInPlayer.discordId);

    useEffect(() => {
        axios.get('/api/matches/' + matchId)
            .then((response) => {
                setMatch(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error('Error retrieving match', error);
            })
    }, [loading, matchId]);

    useEffect(() => {
        if (match.matchState !== 'SIGNUPS') {
            axios.get('/api/matches/' + matchId + '/public_objectives')
                .then((response) => {
                    setPublicObjectives(response.data);
                })
                .catch((error) => {
                    console.error('Error retrieving match objectives', error);
                })
        }
    }, [match, matchId]);

    const signupToPlayerSection = (signup) => {
        return (
            <List.Item key={signup.playerId}>
                <PlayerAvatar player={signup.player} size='mini'/>
                <List.Content>
                    <List.Header>{signup.player.discordUsername}</List.Header>
                </List.Content>
            </List.Item>
        );
    }

    const sectionColors = ['red', 'green', 'blue', 'orange', 'purple', 'teal', 'violet', 'yellow', 'pink', 'grey', 'black',
        'red', 'green', 'blue', 'orange', 'purple', 'teal', 'violet', 'yellow', 'pink', 'grey'];

    const playerSections = match.signups && match.signups.map((signup, index) =>
        <Segment key={index} inverted color={sectionColors[index]}>
            <PlayerAvatar player={signup.player} size='mini' floated='right'/>
            <Header style={{'marginTop': '0em'}}>{signup.player.discordUsername}</Header>
            <MatchCivViewer signup={signup} loggedInPlayer={loggedInPlayer}/>
        </Segment>
    );
    const publicObjectiveSections = publicObjectives.map(objective => <ObjectiveCard key={objective.objectiveName} objectiveJson={objective} />);

    return (
        <React.Fragment>
            <Container style={{marginTop: '6em'}}>
                <Header as='h2'>{match.matchName}</Header>

                {loading &&
                <Loader/>
                }

                {!loading &&
                <React.Fragment>

                    <MatchHeader match={match} loggedInPlayer={loggedInPlayer} onMatchUpdated={() => setLoading(true)}/>

                    {match.matchState === 'SIGNUPS' && match.signups.map(signupToPlayerSection)}

                    {match.matchState !== 'SIGNUPS' &&
                        <React.Fragment>
                            <MapSettings match={match}/>

                            <Container style={{'margin': '1em'}}>
                                <Header>Public objectives:</Header>
                                <p>The first player to claim 5 stars wins the game. These may be any mix of public and secret objectives.</p>
                                <CardGroup>
                                    {publicObjectiveSections}
                                </CardGroup>
                            </Container>


                        </React.Fragment>
                    }

                    {match.matchState === 'DRAFT' &&
                    <React.Fragment>
                        <p>Committed players: {match.signups.filter(s => s.committed).length}</p>
                        <List horizontal>
                            {match.signups.filter(s => s.committed).map(signupToPlayerSection)}
                        </List>
                        <p>Players still to commit: {match.signups.filter(s => !s.committed).length}</p>
                        <List horizontal>
                            {match.signups.filter(s => !s.committed).map(signupToPlayerSection)}
                        </List>

                        {currentPlayerSignup &&
                        <React.Fragment>
                            <Header as='h3'>{loggedInPlayer.discordUsername}'s civ</Header>

                            <MatchCivBuilder match={match} loggedInPlayer={loggedInPlayer}
                                             onCommitChange={() => setLoading(true)}/>

                        </React.Fragment>
                        }

                    </React.Fragment>
                    }

                    {match.matchState === 'IN_PROGRESS' &&
                    <Container>
                        <DownloadMatchModButton match={match} />

                        {playerSections}

                        <DownloadMatchModButton match={match} />
                    </Container>
                    }

                </React.Fragment>
                }
            </Container>
        </React.Fragment>
    );
};


export default MatchPage;