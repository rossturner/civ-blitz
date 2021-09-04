import {Button, Checkbox, Container, Header, List} from "semantic-ui-react";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {useHistory} from "react-router-dom";
import DlcSettingsUpdateModal from "./DlcSettingsUpdateModal";


const DlcSettingsPage = ({loggedInPlayer}) => {

    const [playerSettings, setPlayerSettings] = useState([]);
    const [showUpdateModal, setShowUpdateModal] = useState(false);

    useEffect(() => {
        axios.get('/api/dlc')
            .then(response => {
                setPlayerSettings(response.data)
            })
            .catch(console.error);
    }, []);

    const toggleDlc = (dlcName) => {
        const newSettings = playerSettings.map(setting => {
            if (setting.dlcName === dlcName) {
                return {...setting, enabled: !setting.enabled}
            } else {
                return setting;
            }
        })
        setPlayerSettings(newSettings);
    };

    const checkboxes = playerSettings.map(setting =>
        <List.Item key={setting.dlcName}>
            <Checkbox label={setting.dlcName} checked={setting.enabled} onClick={() => toggleDlc(setting.dlcName)} />
        </List.Item>
        );

    const history = useHistory();
    const settingsConfirmed = () => {
        history.push("/collection");
    };

    return (
        <React.Fragment>
            <Container>
                <Header as='h2'>DLC Settings</Header>

                {!loggedInPlayer &&
                <p>You must be logged in to access this page</p>
                }

                {loggedInPlayer &&
                <React.Fragment>
                    <p>Use this page to select which Civ 6 DLC you <b>do own</b>.
                        This will limit the cards you receive to only those that you should be able to use.</p>
                    <p><b>The Gathering Storm expansion is required</b>. Contact an admin if you have filled out this page incorrectly.</p>

                    {playerSettings.length > 0 &&
                    <React.Fragment>
                        <List>
                            {checkboxes}
                        </List>
                        <Button primary onClick={() => setShowUpdateModal(true)}>Save</Button>
                    </React.Fragment>
                    }

                    <DlcSettingsUpdateModal showModal={showUpdateModal} playerSettings={playerSettings} onConfirm={settingsConfirmed}
                        onCancel={() => setShowUpdateModal(false)}/>

                </React.Fragment>
                }

            </Container>
        </React.Fragment>
    )
};

export default DlcSettingsPage;