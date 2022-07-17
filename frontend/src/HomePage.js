import {Button, Container, Header, Icon} from "semantic-ui-react";


const HomePage = () => {

    return (
        <Container>
            <Container textAlign='center' style={{marginBottom: '1em'}}>
                <Header as='h2'>Welcome to Civ Blitz!</Header>
            </Container>
            <Container text>
                <p>
                    This site is the home of Civ Blitz, which is an exiciting new multiplayer game mode for
                    Civilization VI where players own a collection of cards representing traits from civilizations
                    in the base game, e.g. civ abilities, leader abilities, unique units and infrastructure. Players
                    sign up to a multiplayer match, build a unique civ with a combination of 4 of these cards, and play
                    out the match using their custom civs. Blitz matches have objective-based victory conditions (i.e.
                    the first player to claim a number of public and secret objectives wins the game) and are
                    played at "quick" speed, to create an interesting short format game (lasting 80 or 100 turns).
                    The cards used are removed from the player's collection,
                    they gain a new set of 4 cards to replace them, and doing well in the match rewards the player with
                    points which can be spent to further grow their collection of cards.
                </p>
                <p>
                    Civ Blitz is a creation of <a href='https://discord.gg/Bq6p7yJHKd' target='_blank' rel="noreferrer">the Disczord community</a>,
                    home of Twitch streamer <a href='https://www.twitch.tv/harringzord' target='_blank' rel="noreferrer">Harringzord</a>.
                </p>
                <p align='center'>
                    <Button size='large' primary as='a' href='https://docs.google.com/document/d/1hQVetUjOEpe80q44MxvCVJGFkCYiWrA8Gfh05yCYyZM/edit?usp=sharing' target='_blank'>
                        <Icon name='file' />
                        Civ Blitz Multiplayer Rules
                    </Button>
                </p>
                <p align='center'>
                    <Button size='large' as='a' href='https://discord.gg/Bq6p7yJHKd' target='_blank'>
                        <Icon name='discord' />
                        Join the Discord Server
                    </Button>
                </p>
                <p align='center'>
                    <iframe src="https://discord.com/widget?id=826128455137361980&theme=dark" width="350" height="500"
                            allowTransparency="true" frameBorder="0"
                            sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"></iframe>
                </p>
            </Container>
        </Container>
    );
};

export default HomePage;