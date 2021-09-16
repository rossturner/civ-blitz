import {List} from "semantic-ui-react";
import React from 'react';

const MapSettings = ({match}) => {

    return (
        <React.Fragment>
            <List horizontal>
                <List.Item>
                    <List.Content>
                        <List.Header>Num turns</List.Header>
                        {match.numTurns}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>Start Era</List.Header>
                        {match.startEra}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>Map Type</List.Header>
                        {match.mapType.replaceAll('_', ' ')}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>Map Size</List.Header>
                        {match.mapSize}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>World Age</List.Header>
                        {match.worldAge}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>Sea Level</List.Header>
                        {match.seaLevel}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>Temperature</List.Header>
                        {match.temperature}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>Rainfall</List.Header>
                        {match.rainfall}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>City States</List.Header>
                        {match.cityStates}
                    </List.Content>
                </List.Item>
                <List.Item>
                    <List.Content>
                        <List.Header>Disaster Intensity</List.Header>
                        {match.disasterIntensity}
                    </List.Content>
                </List.Item>
            </List>
            {match.startEraHint && <p><i>Hint: {match.startEraHint}</i></p>}
        </React.Fragment>
    );
}

export default MapSettings;