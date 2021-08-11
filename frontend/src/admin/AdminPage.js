import {Button, Container, Header, Input, Segment} from "semantic-ui-react";
import React, {useState} from "react";
import axios from "axios";
import {useHistory} from "react-router-dom";


const AdminPage = () => {

    const [matchTimeslot, setMatchTimeslot] = useState('');
    let history = useHistory();

    const createMatch = () => {
        axios.post('/api/matches', {timeslot: matchTimeslot})
            .then((response) => {
                history.push("/matches");
            })
            .catch((error) => {
                console.error('Error creating match', error);
            })
    };

    return (
        <React.Fragment>
            <Container style={{marginTop: '6em'}}>
                <Header as='h2'>Admin area</Header>

                <Segment>
                    <Header as='h3'>Create a new match for signups</Header>

                    <Container>
                        <Input value={matchTimeslot} onChange={(event, data) => setMatchTimeslot(data.value)} fluid placeholder='Describe the timeslot this match will be played in' />
                    </Container>

                    <Button primary onClick={createMatch} style={{'marginTop': '1em'}} disabled={matchTimeslot.length === 0}>Create match</Button>
                </Segment>
            </Container>
        </React.Fragment>
    );
};


export default AdminPage;