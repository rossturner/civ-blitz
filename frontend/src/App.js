import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import React, {useEffect, useState} from "react";
import CardStore from "./cards/CardStore";
import CivBuilder from "./CivBuilder";
import ImpRandom from "./ImpRandom";
import ModTester from "./ModTester";
import PlayerCollection from "./PlayerCollection";
import jwt from "jsonwebtoken";
import {Route, Switch, withRouter} from "react-router-dom";
import axios from 'axios';

const App = ({history}) => {

    const [collection, setCollection] = useState([]);
    const [loading, setLoading] = useState(true);
    const [loggedInPlayer, setLoggedInPlayer] = useState();

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        let jsonWebToken = urlParams.get('token');
        if (jsonWebToken) {
            window.localStorage.setItem('token', jsonWebToken);
        } else {
            jsonWebToken = window.localStorage.getItem('token');
            // TODO check for expiry and trigger refresh of token here
        }

        if (jsonWebToken) {
            axios.defaults.headers.common['Authorization'] = jsonWebToken;
            const decoded = jwt.decode(jsonWebToken);
            setLoggedInPlayer({
                discordUsername: decoded.username,
                discordId: decoded.sub
            });
            history.push('/collection');
        }


        axios.get("/api/cards")
            .then((response) => {
                if (!CardStore.initialised) {
                    CardStore.addCards(response.data);
                    let newCollection = CardStore.getInitialCollection();
                    newCollection.sort(ImpRandom.cardSort);
                    setLoading(false);
                    setCollection(newCollection);
                }
            })
            .catch((error) => {
                console.error('Error loading cards', error);
            });

    }, []);

    if (loading) {
        return <div>Loading...</div>
    }
    return (
        <div>
            <TopLevelMenu loggedInPlayer={loggedInPlayer}/>

            <Switch>
                <Route path="/collection">
                    <PlayerCollection loggedInPlayer={loggedInPlayer}/>
                </Route>
                <Route path="/civbuilder">
                    <CivBuilder collection={collection} setCollection={setCollection}/>
                </Route>
                <Route path="/">
                    <ModTester/>
                </Route>
            </Switch>

        </div>
    );
}

export default withRouter(App);
