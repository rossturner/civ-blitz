import {Table} from "semantic-ui-react";

const MapSettings = ({match}) => {

    return (
        <Table basic='very' celled collapsing>
            <Table.Body>
                <Table.Row>
                    <Table.Cell>Map Type</Table.Cell>
                    <Table.Cell>{match.mapType.replaceAll('_', ' ')}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>Map Size</Table.Cell>
                    <Table.Cell>{match.mapSize}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>World Age</Table.Cell>
                    <Table.Cell>{match.worldAge}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>Sea Level</Table.Cell>
                    <Table.Cell>{match.seaLevel}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>Temperature</Table.Cell>
                    <Table.Cell>{match.temperature}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>Rainfall</Table.Cell>
                    <Table.Cell>{match.rainfall}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>City States</Table.Cell>
                    <Table.Cell>{match.cityStates}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>Disaster Intensity</Table.Cell>
                    <Table.Cell>{match.disasterIntensity}</Table.Cell>
                </Table.Row>
            </Table.Body>
        </Table>
    );
}

export default MapSettings;