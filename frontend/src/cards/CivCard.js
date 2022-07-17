import {Card, Image} from "semantic-ui-react";
import './CivCard.css'
import CardInfo from "./CardInfo";
import CardStore from "./CardStore";
import React from "react";
import PropTypes from 'prop-types';

const CivCard = ({cardJson, onClick, clickDisabled}) => {

    const footer = (
        <div>
            <Image src={process.env.PUBLIC_URL + '/media/'+CardStore.getMediaNameForCivType(cardJson.civilizationType)+'.png'} width={30} />
            &nbsp;
            {cardJson.civilizationFriendlyName}
         </div>);

    const header = (
        <Card.Header>
            {cardJson.enhancedCardName || cardJson.baseCardName}
            {cardJson.quantity > 1 &&
            <React.Fragment>&nbsp;(x{cardJson.quantity})</React.Fragment>
            }
            {cardJson.superCategory === 'Upgrade' &&
                <h5>Upgrade of {cardJson.baseCardName}</h5>
            }
            <Image src={process.env.PUBLIC_URL + '/media/'+cardJson.mediaName+'.png'} />
        </Card.Header>
    );

    let description = <span>
        {cardJson.baseCardDescription}
        {cardJson.enhancedCardDescription &&
            <p style={{color: 'blue'}}>{cardJson.enhancedCardDescription}</p>
        }
        {cardJson.freeUseCard &&
        <p>
            <i>This card grants an optional free use of the {cardJson.freeUseCard.baseCardName} card.</i>
        </p>
        }
    </span>;

    return <Card
        className={'civ-card ' + cardJson.rarity}
        onClick={clickDisabled ? null : () => onClick(cardJson)}
        color={CardInfo.getCategoryColor(cardJson.cardCategory)}
        header={header}
        meta={cardJson.rarity + ' ' + CardInfo.getCategoryName(cardJson.cardCategory)}
        description={description}
        extra={footer}
    />
};

CivCard.propTypes = {
    cardJson: PropTypes.object,
    onClick: PropTypes.func,
    clickDisabled: PropTypes.bool
}

CivCard.defaultPropts = {
    clickDisabled: false
}

export default CivCard;