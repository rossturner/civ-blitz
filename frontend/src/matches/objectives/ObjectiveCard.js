import {Card, Icon} from "semantic-ui-react";
import './ObjectiveCard.css';
import PlayerAvatar from "../../player/PlayerAvatar";


const ObjectiveCard = ({objectiveJson, cardClicked, clickDisabled, claimedByPlayers}) => {

    let className = 'objective-card';
    let color = undefined;
    const isSecretObjective = objectiveJson.type === 'SECRET';
    if (isSecretObjective) {
        if (objectiveJson.selected) {
            color = 'red';
        } else {
            className += ' unselected';
        }
    }

    const claimantSections = claimedByPlayers.map(signup => <PlayerAvatar key={signup.player.playerId} player={signup.player} size='huge' />);

    return (
        <Card className={className} onClick={!clickDisabled ? () => cardClicked(objectiveJson) : null} color={color}>
            <Card.Content>
                <Card.Header>{objectiveJson.objectiveName}</Card.Header>
                {isSecretObjective &&
                <Card.Meta>Secret objective</Card.Meta>
                }
                <Card.Description>
                    {objectiveJson.description}
                </Card.Description>
            </Card.Content>
            {claimedByPlayers.length > 0 &&
                <Card.Content extra>
                    {claimantSections}
                </Card.Content>
            }
            <Card.Content extra>
                <Icon color='yellow' name='star' />
                {objectiveJson.numStars > 1 &&
                <Icon color='yellow' name='star' />
                }
                {objectiveJson.numStars > 2 &&
                <Icon color='yellow' name='star' />
                }
                {objectiveJson.numStars} star{objectiveJson.numStars > 1 ? 's' : ''}
            </Card.Content>
        </Card>
    )
};

ObjectiveCard.defaultProps = {
    claimedByPlayers: []
};

export default ObjectiveCard;
