import {useEffect, useState} from "react";
import {Button, Modal} from "semantic-ui-react";
import CivCard from "../cards/CivCard";


const EditMatchModal = ({cardWithFreeUseCard, onConfirm, onCancel}) => {

    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        if (cardWithFreeUseCard.freeUseCard) {
            setShowModal(true);
        }
    }, [cardWithFreeUseCard]);

    return (
        <Modal
            onClose={() => setShowModal(false)}
            open={showModal}
        >
            <Modal.Header>Do you want to use this card for free?</Modal.Header>
            <Modal.Content>
                <Modal.Description>
                    <p>Adding {cardWithFreeUseCard.cardName} comes with a free use of the {cardWithFreeUseCard.freeUseCard && cardWithFreeUseCard.freeUseCard.cardName} card. Do you wish to add this card as well?</p>
                    {cardWithFreeUseCard.freeUseCard &&
                    <CivCard cardJson={cardWithFreeUseCard.freeUseCard} clickDisabled={true}/>
                    }
                </Modal.Description>
            </Modal.Content>
            <Modal.Actions>
                <Button onClick={() =>  {setShowModal(false); onCancel();}}>No</Button>
                <Button onClick={() => {setShowModal(false); onConfirm();}} primary>Yes</Button>
            </Modal.Actions>
        </Modal>
    );
};


export default EditMatchModal;