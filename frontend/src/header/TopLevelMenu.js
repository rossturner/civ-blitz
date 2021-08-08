import {Container, Image, Menu} from "semantic-ui-react";
import React from "react";


const TopLevelMenu = ({loggedInPlayer, onItemClick}) => {

    return (
        <Menu fixed='top' inverted>
            <Container>
                <Menu.Item as='a'>
                    {/*<Image size='mini' src='/logo.png' style={{ marginRight: '1.5em' }} />*/}
                    Civ Imperium
                </Menu.Item>

                {!loggedInPlayer &&
                    <Menu.Item as='a' header href='/oauth2/authorization/discord'>
                        <Image src='/images/Discord-Logo-Color.png' height={20}/>
                        &nbsp;
                        Login
                    </Menu.Item>
                }
                {loggedInPlayer &&
                    <Menu.Item as='a' header onClick={() => onItemClick('collection')}>
                        {loggedInPlayer.discordUsername}'s Collection
                    </Menu.Item>
                }

                <Menu.Item as='a' header onClick={() => onItemClick('modtester')}>
                    Mod Tester
                </Menu.Item>
                <Menu.Item as='a' header onClick={() => onItemClick('civbuilder')}>
                    Civ Builder Proof of Concept
                </Menu.Item>

            </Container>
        </Menu>
    );
};

export default TopLevelMenu;