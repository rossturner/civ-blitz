import {Button, CardGroup, Container, Header, List, Loader, Segment} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import PlayerAvatar from "../player/PlayerAvatar";
import {useHistory, useParams} from "react-router-dom";
import MatchHeader from "./MatchHeader";
import MatchCivBuilder from "./MatchCivBuilder";
import MapSettings from "./MapSettings";
import MatchCivViewer from "./MatchCivViewer";
import DownloadMatchModButton from "./DownloadMatchModButton";
import ObjectiveCard from "./objectives/ObjectiveCard";
import ClaimObjectiveModal from "./objectives/ClaimObjectiveModal";
import UnclaimObjectiveModal from "./objectives/UnclaimObjectiveModal";
import AdminClaimObjectiveModal from "./objectives/AdminClaimObjectiveModal";
import AdminUnclaimObjectiveModal from "./objectives/AdminUnclaimObjectiveModal";
import MatchLeaderboard from "./MatchLeaderboard";

const MatchPage = ({loggedInPlayer}) => {

    const {matchId} = useParams();
    const [loading, setLoading] = useState(true);
    const [match, setMatch] = useState({});
    const [leaderboard, setLeaderboard] = useState({});
    const [publicObjectives, setPublicObjectives] = useState([]);
    const [secretObjectives, setSecretObjectives] = useState([]);

    const [claimingObjective, setClaimingObjective] = useState({});
    const [unclaimingObjective, setUnclaimingObjective] = useState({});
    const [showAdminClaimObjectiveModal, setShowAdminClaimObjectiveModal] = useState(false);
    const [showAdminUnclaimObjectiveModal, setShowAdminUnclaimObjectiveModal] = useState(false);

    const currentPlayerSignup = match.signups && match.signups.find(s => s.playerId === loggedInPlayer.discordId);
    const playerIsAdminNotInMatch = !currentPlayerSignup && loggedInPlayer.isAdmin;

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

        if (match.matchState === 'IN_PROGRESS' || match.matchState === 'POST_MATCH' || match.matchState === 'COMPLETED') {
            axios.get('/api/matches/' + matchId + '/all_secret_objectives')
                .then((response) => {
                    setSecretObjectives(response.data);
                })
                .catch(() => {
                    // Ignore these errors, player might be taking part in this match
                });
            axios.get('/api/matches/' + matchId + '/leaderboard')
                .then((response) => {
                    setLeaderboard(response.data);
                })
                .catch(() => {
                    // Ignore these errors, might be draft phase or something
                });
        }
    }, [match, matchId, currentPlayerSignup, loggedInPlayer]);

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

    const secretObjectiveClicked = (objective) => {
        if (objective.claimed) {
            setUnclaimingObjective(objective);
        } else {
            setClaimingObjective(objective);
        }
    };

    const playerSections = match.signups && match.signups.map((signup, index) =>
        <Segment key={index} inverted color={sectionColors[index]}>
            <PlayerAvatar player={signup.player} size='mini' floated='right'/>
            <Header style={{'marginTop': '0em'}}>{signup.player.discordUsername}</Header>
            <MatchCivViewer match={match} signup={signup} loggedInPlayer={loggedInPlayer}
                            secretObjectiveClicked={secretObjectiveClicked}
                            secretObjectivesProp={secretObjectives.filter(s => s.playerId === signup.playerId)}/>
        </Segment>
    );
    let publicObjectiveSections = [];
    if (match.matchId) {
        publicObjectiveSections = publicObjectives.map(objective => {
            const claimedByPlayers = objective.claimedByPlayerIds.map(id => match.signups.find(s => s.playerId === id));
            const objectiveClicked = (objective) => {
                if (objective.claimedByPlayerIds.includes(loggedInPlayer.discordId)) {
                    setUnclaimingObjective(objective);
                } else {
                    setClaimingObjective(objective);
                }
            };
            const clickDisabled = match.matchState !== 'IN_PROGRESS' || !currentPlayerSignup;
            return <ObjectiveCard key={objective.objectiveName} cardClicked={objectiveClicked}
                                  clickDisabled={clickDisabled}
                                  objectiveJson={objective} claimedByPlayers={claimedByPlayers} />;
        });
    }

    const history = useHistory();
    const onMatchDeleted = () => {
        history.push("/matches");
    }

    return (
        <React.Fragment>
            <Container style={{marginTop: '6em'}}>
                <Header as='h2'>{match.matchName}</Header>

                {loading &&
                <Loader/>
                }

                {!loading &&
                <React.Fragment>

                    <MatchHeader match={match} loggedInPlayer={loggedInPlayer} onMatchUpdated={() => setLoading(true)}
                        onMatchDeleted={onMatchDeleted}/>

                    {match.matchState === 'SIGNUPS' && match.signups.map(signupToPlayerSection)}

                    {match.matchState !== 'SIGNUPS' &&
                        <React.Fragment>
                            <MapSettings match={match}/>

                            {leaderboard &&
                            <Container style={{'margin': '1em'}}>
                                <Header>Leaderboard:</Header>
                                <p>The first player to claim 7 stars wins the game. These may be any mix of public and secret objectives.</p>
                                <MatchLeaderboard match={match} leaderboard={leaderboard} loggedInPlayer={loggedInPlayer}
                                    leaderboardChanged={setLeaderboard}/>
                            </Container>
                            }

                            <Container style={{'margin': '1em'}}>
                                <Header>Public objectives:</Header>
                                <p>Each 1 star objective can be claimed by up to 3 players. Each 2 star objective can be claimed by up to 2 players.
                                    Each 3 star objective can be claimed by a single player.</p>
                                <CardGroup centered>
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

                    {(match.matchState === 'IN_PROGRESS' || match.matchState === 'POST_MATCH'|| match.matchState === 'COMPLETED') &&
                    <Container>
                        <DownloadMatchModButton match={match} />
                        {playerIsAdminNotInMatch && match.matchState === 'IN_PROGRESS' &&
                            <React.Fragment>
                                <Button color='violet' onClick={() => setShowAdminClaimObjectiveModal(true)}>Claim an objective for a player</Button>
                                <Button color='purple' onClick={() => setShowAdminUnclaimObjectiveModal(true)}>Remove a claimed objective from a player</Button>
                            </React.Fragment>
                        }

                        {playerSections}

                        <DownloadMatchModButton match={match} />
                    </Container>
                    }


                    <ClaimObjectiveModal match={match} objective={claimingObjective}
                                             onConfirm={() => {setLoading(true);setClaimingObjective({});}}
                                             onCancel={() => setClaimingObjective({})} />
                    <UnclaimObjectiveModal match={match} objective={unclaimingObjective}
                                         onConfirm={() => {setLoading(true);setUnclaimingObjective({});}}
                                         onCancel={() => setUnclaimingObjective({})} />
                    <AdminClaimObjectiveModal match={match} open={showAdminClaimObjectiveModal}
                                              publicObjectives={publicObjectives}
                                              secretObjectives={secretObjectives}
                                              onConfirm={() => {setLoading(true);setShowAdminClaimObjectiveModal(false)}}
                                              onCancel={() => setShowAdminClaimObjectiveModal(false)}/>
                    <AdminUnclaimObjectiveModal match={match} open={showAdminUnclaimObjectiveModal}
                                              publicObjectives={publicObjectives}
                                              secretObjectives={secretObjectives}
                                              onConfirm={() => {setLoading(true);setShowAdminUnclaimObjectiveModal(false)}}
                                              onCancel={() => setShowAdminUnclaimObjectiveModal(false)}/>

                </React.Fragment>
                }
            </Container>
        </React.Fragment>
    );
};


export default MatchPage;