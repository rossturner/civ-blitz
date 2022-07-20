import {Container, Header} from "semantic-ui-react";


const DraftMatchPage = ({loggedInPlayer}) => {


    return (
        <Container>
            <Container textAlign='center' style={{marginBottom: '1em'}}>
                <Header as='h2'>Civ Blitz Draft Mode</Header>
            </Container>
            <Container text>
                <p>
                    You can use this page to generate a one-off Civ Blitz Draft game for you and your friends.
                    Each player is given a specific number of cards to choose from (rather than all of those possible)
                    and then builds a civ out of those cards to be used in a game.
                </p>


                {!loggedInPlayer &&
                    <p>You must <b>log in with Discord</b> (using the menu bar) to create or sign up to a Civ Blitz Draft game.</p>
                }

                {loggedInPlayer &&
                    <p>You are logged in!</p>
                }

                {/*    TODO if player has a draft in progress, link to that */}

                {/*    TODO is player does not have a draft in progress, allow creation of new*/}
            </Container>

        </Container>
    );
}

export default DraftMatchPage;