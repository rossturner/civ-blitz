import {Button, Card, Icon, Message} from "semantic-ui-react";
import React, {useState} from "react";
import axios from "axios";
import {useHistory} from "react-router-dom";


const BoosterPackPurchase = () => {

    const [errorText, setErrorText] = useState('');
    const history = useHistory();

    const doPurchase = () => {
        setErrorText('');
        axios.post('/api/cards/purchase', {
            type: 'MATCH_BOOSTER'
        }).then(response => {
            history.push("/packs");
        }).catch(error => {
            setErrorText(error.response.data.message);
        });
    }

    return (
        <Card>
            <Card.Content>
                <Card.Header>Booster Pack</Card.Header>
                <Card.Meta>All card categories</Card.Meta>
                <Card.Description>
                    <Icon color='yellow' name='star' />
                    <Icon color='yellow' name='star' />
                    <Icon color='yellow' name='star' />
                    <Icon color='yellow' name='star' />
                    <Icon color='yellow' name='star' />
                    Costs 5 stars
                </Card.Description>
            </Card.Content>
            <Card.Content>
                <Card.Description>
                    4x cards, one from each category
                </Card.Description>
            </Card.Content>
            <Card.Content extra>
                <Button color='green' style={{'marginTop': '1em'}} onClick={doPurchase}>
                    <Icon name='shop'/>
                    Purchase
                </Button>
                {errorText &&
                <Message error>
                    {errorText}
                </Message>
                }
            </Card.Content>
        </Card>
    )
}

export default BoosterPackPurchase;