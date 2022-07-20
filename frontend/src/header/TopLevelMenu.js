import {Image, Menu} from "semantic-ui-react";
import React from "react";
import {Link} from "react-router-dom";

const TopLevelMenu = ({loggedInPlayer}) => {

    const randomString = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
    document.cookie = 'state='+randomString;

    let baseUrl = (window.location.protocol + '//' + window.location.host).replace('3000', '8080');
    const encodedBaseUrl = encodeURIComponent(baseUrl);
    const discordLoginUrl = 'https://discord.com/api/oauth2/authorize?client_id=873320729351958539&redirect_uri='+
        encodedBaseUrl+'%2Fapi%2Flogin%2Fdiscord&response_type=code&scope=identify&state='+randomString;

    return (
        <Menu stackable inverted className='Top-level-menu'>
            <Menu.Item>
                <Link to='/'>
                {/*<Image size='mini' src='/logo.png' style={{ marginRight: '1.5em' }} />*/}
                Civ Blitz
                </Link>
            </Menu.Item>

            {!loggedInPlayer &&
                <Menu.Item as='a' header href={discordLoginUrl}>
                    <Image src='/images/Discord-Logo-Color.png' height={20}/>
                    &nbsp;
                    Login
                </Menu.Item>
            }
            {loggedInPlayer &&
                <Menu.Item header>
                    <Link to='/collection'>
                        {loggedInPlayer.discordUsername}'s Collection
                    </Link>
                </Menu.Item>
            }
            {loggedInPlayer &&
            <Menu.Item header>
                <Link to='/matches'>
                    Matches
                </Link>
            </Menu.Item>
            }

            <Menu.Item header>
                <Link to='/leaderboard'>
                    Leaderboard
                </Link>
            </Menu.Item>

            <Menu.Item header>
                <Link to='/modtester'>
                Mod Tester
                </Link>
            </Menu.Item>

            <Menu.Item header>
                <Link to='/draft'>
                    Draft Mode
                </Link>
            </Menu.Item>

            {loggedInPlayer &&
            <Menu.Item header>
                <Link to='/dlc-settings'>
                    {loggedInPlayer.discordUsername}'s DLC Settings
                </Link>
            </Menu.Item>
            }

            {loggedInPlayer && loggedInPlayer.isAdmin &&
            <Menu.Item header>
                <Link to='/admin'>
                    Admin area
                </Link>
            </Menu.Item>
            }
            <Menu.Item header>
                <Link to='/knownbugs'>
                    Known Bugs
                </Link>
            </Menu.Item>
        </Menu>
    );
};

export default TopLevelMenu;