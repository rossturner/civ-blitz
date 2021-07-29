import {Card, Icon} from "semantic-ui-react";
import './ImperiumCard.css'
import CardInfo from "./CardInfo";

const ImperiumCard = ({cardJson, onClick}) => {

    const footer = (
        <div>
             <Icon name='flag'/>
            {cardJson.civilizationFriendlyName}
         </div>);

    return <Card
        className='imperium-card'
        link
        onClick={onClick}
        color={CardInfo.getCategoryColor(cardJson.cardCategory)}
        header={cardJson.cardName}
        meta={CardInfo.getCategoryName(cardJson.cardCategory)}
        description={cardJson.cardDescription}
        extra={footer}
    />
};

export default ImperiumCard;