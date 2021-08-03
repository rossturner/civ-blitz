import {Container, Menu} from "semantic-ui-react";
import React from "react";


const TopLevelMenu = ({onItemClick}) => {

    return (
        <Menu fixed='top' inverted>
            <Container>
                <Menu.Item as='a'>
                    {/*<Image size='mini' src='/logo.png' style={{ marginRight: '1.5em' }} />*/}
                    Civ Imperium
                </Menu.Item>
                <Menu.Item as='a' header onClick={() => onItemClick('civbuilder')}>
                    Civ Builder Proof of Concept
                </Menu.Item>
                <Menu.Item as='a' header onClick={() => onItemClick('modtester')}>
                    Mod Tester
                </Menu.Item>

                {/*<Dropdown item simple text='Dropdown'>*/}
                {/*    <Dropdown.Menu>*/}
                {/*        <Dropdown.Item>List Item</Dropdown.Item>*/}
                {/*        <Dropdown.Item>List Item</Dropdown.Item>*/}
                {/*        <Dropdown.Divider />*/}
                {/*        <Dropdown.Header>Header Item</Dropdown.Header>*/}
                {/*        <Dropdown.Item>*/}
                {/*            <i className='dropdown icon' />*/}
                {/*            <span className='text'>Submenu</span>*/}
                {/*            <Dropdown.Menu>*/}
                {/*                <Dropdown.Item>List Item</Dropdown.Item>*/}
                {/*                <Dropdown.Item>List Item</Dropdown.Item>*/}
                {/*            </Dropdown.Menu>*/}
                {/*        </Dropdown.Item>*/}
                {/*        <Dropdown.Item>List Item</Dropdown.Item>*/}
                {/*    </Dropdown.Menu>*/}
                {/*</Dropdown>*/}
            </Container>
        </Menu>
    );
};

export default TopLevelMenu;