import {Container, Header} from "semantic-ui-react";


const HomePage = () => {

    return (
        <Container style={{marginTop: '6em'}}>
            <Container textAlign='center' style={{marginBottom: '1em'}}>
                <Header as='h2'>Welcome to Civ Imperium!</Header>
            </Container>
            <Container text>
                <p>
                    This site is the home of Civ Imperium, which is an exiciting new multiplayer game mode for
                    Civilization VI where players own a collection of cards representing traits from civilizations
                    in the base game, e.g. civ abilities, leader abilities, unique units and infrastructure. Players
                    sign up to a multiplayer match, build a unique civ with a combination of 4 of these cards, and play
                    out the match using their custom civs. Imperium matches have objective-based victory conditions (i.e.
                    the first player to claim a number of public and secret objectives wins the game) and will probably be
                    played at "quick" speed, to create an interesting short format game.
                    The cards used are removed from the player's collection,
                    they gain a new set of 4 cards to replace them, and doing well in the match rewards the player with
                    points which can be spent to further grow their collection of cards.
                </p>
                <p>
                    Civ Imperium is a creation of <a href='https://discord.gg/Bq6p7yJHKd' target='_blank' rel="noreferrer">the Disczord community</a>,
                    home of Twitch streamer <a href='https://www.twitch.tv/harringzord' target='_blank' rel="noreferrer">Harringzord</a>.
                </p>
                <p>
                    If that sounds like something you'd like to take part in, join <a href='https://discord.gg/Bq6p7yJHKd' target='_blank' rel="noreferrer">the Discord server</a>,
                    read the welcome page, and sit back for a bit as Civ Imperium is still under development! We're looking at kicking
                    off the first match(es) at the end of August/start of September. Actual game rules for Civ Imperium still to come.
                </p>
            </Container>
        </Container>
    );
};

export default HomePage;