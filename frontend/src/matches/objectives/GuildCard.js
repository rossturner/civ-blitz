import {Card, Icon, Popup} from "semantic-ui-react";
import './ObjectiveCard.css';


const allCategories = {
    'Religious': {
        'color': 'black',
        'icon': 'adjust'
    },
    'Gold': {
        'color': 'yellow',
        'icon': 'money'
    },
    'Science': {
        'color': 'blue',
        'icon': 'flask'
    },
    'Culture': {
        'color': 'purple',
        'icon': 'music'
    },
    'Military': {
        'color': 'red',
        'icon': 'military'
    },
    'Conquest': {
        'color': 'orange',
        'icon': 'bomb'
    },
    'Diplomatic': {
        'color': 'olive',
        'icon': 'conversation'
    },
    'Architecture': {
        'color': 'grey',
        'icon': 'university'
    },
    'Agriculture': {
        'color': 'green',
        'icon': 'leaf'
    },
    'Industry': {
        'color': 'orange',
        'icon': 'cogs'
    },
    'Exploration': {
        'color': 'violet',
        'icon': 'paper plane outline'
    },
    'Empire': {
        'color': 'brown',
        'icon': 'flag'
    },
    'Wildcard': {
        'color': 'pink',
        'icon': 'asterisk'
    }
};


const GuildCard = ({guild}) => {

    const categoryToColor = (guildCategory) => {
        return allCategories[guildCategory].color;
    }

    const categoryToIcon = (guildCategory) => {
        return allCategories[guildCategory].icon;
    }

    return (
        <Card className='objective-card'>
            <Card.Content>
                <Card.Header>
                    {guild.guildName}
                </Card.Header>
                <Card.Description>
                    {guild.description}
                </Card.Description>
            </Card.Content>
            <Card.Content extra>
                <Icon color='yellow' name='star' />
                <Icon color='yellow' name='star' />
                <Icon color='yellow' name='star' />
                &nbsp;/&nbsp;
                <Icon color='yellow' name='star' />
                <Icon color='yellow' name='star' />
                &nbsp;/&nbsp;
                <Icon color='yellow' name='star' />

                <Popup
                    trigger={<Icon className='right floated' color={categoryToColor(guild.category)}
                                   name={categoryToIcon(guild.category)} floated='right' />}
                    content={guild.category}
                    basic
                />
            </Card.Content>
        </Card>
    )
};

export default GuildCard;
