import {Button, Container, Header, Input, Message, Segment, Table} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {useHistory} from "react-router-dom";
import {DateTime} from "luxon";


const AdminPage = () => {

    const [matchName, setMatchName] = useState('');
    const [matchTimeslot, setMatchTimeslot] = useState('');
    const [errorText, setErrorText] = useState('');
    const [auditLogs, setAuditLogs] = useState([]);
    let history = useHistory();

    useEffect(() => {
        axios.get('/api/admin/audit_logs')
            .then((response) => {
                setAuditLogs(response.data)
            })
            .catch((error) => {
                console.error('Error retrieving audit history', error);
            })
    }, []);

    const createMatch = () => {
        setErrorText('');
        axios.post('/api/matches', {matchTimeslot, matchName})
            .then((response) => {
                history.push("/matches");
            })
            .catch((error) => {
                setErrorText(error.response.data.message);
            })
    };

    const auditRows = auditLogs.map((auditItem, index) => {
        return (<Table.Row key={index}>
            <Table.Cell>{auditItem.discordUsername}</Table.Cell>
            <Table.Cell>{auditItem.action}</Table.Cell>
            <Table.Cell>{DateTime.fromISO(auditItem.datetime).toLocaleString(DateTime.DATETIME_MED)}</Table.Cell>
        </Table.Row>);
    });

    return (
        <React.Fragment>
            <Container style={{marginTop: '6em'}}>
                <Header as='h2'>Admin area</Header>

                <Segment>
                    <Header as='h3'>Create a new match for signups</Header>

                    <Input value={matchName} onChange={(event, data) => setMatchName(data.value)} fluid placeholder='(Optional) Name for the match, will be generated if left blank, must be unique' />

                    <Input value={matchTimeslot} onChange={(event, data) => setMatchTimeslot(data.value)} fluid placeholder='Describe the timeslot this match will be played in' />

                    <Button primary onClick={createMatch} disabled={matchTimeslot.length === 0}>Create match</Button>

                    {errorText &&
                    <Message negative>
                        <p>{errorText}</p>
                    </Message>
                    }
                </Segment>


                <Segment>
                    <Header as='h3'>Admin history</Header>

                    <Table celled>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell>User</Table.HeaderCell>
                                <Table.HeaderCell>Action</Table.HeaderCell>
                                <Table.HeaderCell>Date Time</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>

                        <Table.Body>
                            {auditRows}
                        </Table.Body>
                    </Table>
                </Segment>
            </Container>
        </React.Fragment>
    );
};


export default AdminPage;