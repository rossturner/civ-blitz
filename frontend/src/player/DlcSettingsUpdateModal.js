import React, {useEffect, useState} from "react";
import {Button, List, Message, Modal} from "semantic-ui-react";
import axios from "axios";


const DlcSettingsUpdateModal = ({showModal, playerSettings, onConfirm, onCancel}) => {

    const [errorText, setErrorText] = useState('');
    const [cardsToBeRerolled, setCardsToBeRerolled] = useState([]);

    const selectedDlcNames = [];
    playerSettings.forEach(s => {
        if (s.enabled) {
            selectedDlcNames.push(s.dlcName);
        }
    });

    useEffect(() => {
        if (showModal) {
            setErrorText('');
            axios.post('/api/dlc', selectedDlcNames)
                .then(response => {
                    setCardsToBeRerolled(response.data);
                })
                .catch(error => {
                    setErrorText(error.response.data.message);
                })
        }
    }, [showModal, playerSettings]);

    const onProceed = () => {
        axios.post('/api/dlc/confirm', selectedDlcNames)
            .then(onConfirm)
            .catch(error => {
                setErrorText(error.response.data.message);
            })
    };

    const dlcNames = playerSettings.filter(s => s.enabled)
        .map(setting => <List.Item key={setting.dlcName}>{setting.dlcName}</List.Item>);

    const cardNames = cardsToBeRerolled
        .map(card => <List.Item key={card.identifier}>{card.enhancedCardName || card.baseCardName}</List.Item>);

    return (
        <Modal
            onClose={onCancel}
            open={showModal}
        >
            <Modal.Header>Confirm DLC Settings</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <p>You have selected that you own the following DLC items:</p>
                    <List horizontal ordered>
                        {dlcNames}
                    </List>
                </Modal.Description>

                {cardsToBeRerolled.length > 0 &&
                <Modal.Description style={{'marginTop': '1em'}}>
                    <p>This will reroll the following cards from your collection:</p>

                    <List horizontal ordered>
                        {cardNames}
                    </List>
                </Modal.Description>
                }

                {errorText &&
                <Message error>
                    {errorText}
                </Message>
                }
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={onCancel}>Cancel</Button>
                <Button onClick={onProceed} primary>Confirm changes</Button>
            </Modal.Actions>
        </Modal>
    );
};


export default DlcSettingsUpdateModal;