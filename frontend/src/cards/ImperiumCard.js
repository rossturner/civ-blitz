import {Card, Image} from "semantic-ui-react";
import './ImperiumCard.css'
import CardInfo from "./CardInfo";
import CardStore from "./CardStore";
import React from "react";
import PropTypes from 'prop-types';

const ImperiumCard = ({cardJson, onClick, clickDisabled}) => {

    const footer = (
        <div>
            <Image src={process.env.PUBLIC_URL + '/media/'+CardStore.getMediaNameForCivType(cardJson.civilizationType)+'.png'} width={30} />
            &nbsp;
            {cardJson.civilizationFriendlyName}
         </div>);

    const header = (
        <Card.Header>
            {cardJson.cardName}
            {cardJson.quantity > 1 &&
            <React.Fragment>&nbsp;(x{cardJson.quantity})</React.Fragment>
            }
            <Image src={process.env.PUBLIC_URL + '/media/'+cardJson.mediaName+'.png'} />
        </Card.Header>
    );

    return <Card
        className='imperium-card'
        onClick={clickDisabled ? null : () => onClick(cardJson)}
        color={CardInfo.getCategoryColor(cardJson.cardCategory)}
        header={header}
        meta={CardInfo.getCategoryName(cardJson.cardCategory)}
        description={cardJson.cardDescription}
        extra={footer}
    />
};

ImperiumCard.propTypes = {
    cardJson: PropTypes.object,
    onClick: PropTypes.func,
    clickDisabled: PropTypes.bool
}

ImperiumCard.defaultPropts = {
    clickDisabled: false
}

export default ImperiumCard;