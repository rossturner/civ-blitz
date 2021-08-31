import {Button, Card, Icon, Message, Select} from "semantic-ui-react";
import React, {useState} from "react";
import CardInfo from "../CardInfo";
import axios from "axios";
import {useHistory} from "react-router-dom";


const TripleCardPurchase = () => {

    const categoryOptions = CardInfo.getAllCategories().map(cat => {
        return {
            key: cat,
            value: cat,
            text: CardInfo.getCategoryName(cat)
        }
    });

    const [selectedCategory, setSelectedCategory] = useState();
    const [errorText, setErrorText] = useState('');
    const history = useHistory();

    const doPurchase = () => {
        setErrorText('');
        axios.post('/api/cards/purchase', {
            type: 'MULTIPLE_CARDS',
            category: selectedCategory
        }).then(response => {
            history.push("/packs");
        }).catch(error => {
            setErrorText(error.response.data.message);
        });
    }

    return (
        <Card>
            <Card.Content>
                <Card.Header>Triple card</Card.Header>
                <Card.Meta>Selected category</Card.Meta>
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
                    3x cards in any category of your choice
                </Card.Description>
            </Card.Content>
            <Card.Content extra>
                <Select placeholder='Select a card category' style={{'minHeight': '1em'}}
                        onChange={(event, {value}) => setSelectedCategory(value)}
                        options={categoryOptions} />
                <Button color='green' style={{'marginTop': '1em'}} disabled={!selectedCategory} onClick={doPurchase}>
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

export default TripleCardPurchase;