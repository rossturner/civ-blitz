import './App.css';
import TopLevelMenu from "./header/TopLevelMenu";
import React, {useEffect, useState} from "react";
import CardStore from "./cards/CardStore";
import ModTester from "./ModTester";
import PlayerCollection from "./PlayerCollection";
import jwt from "jsonwebtoken";
import {Route, Switch, withRouter} from "react-router-dom";
import axios from 'axios';
import Footer from "./header/Footer";
import AdminPage from "./admin/AdminPage";
import MatchesPage from "./matches/MatchesPage";
import HomePage from "./HomePage";
import MatchPage from "./matches/MatchPage";
import PackOpener from "./cards/PackOpener";
import PackShopPage from "./cards/PackShopPage";
import DlcSettingsPage from "./player/DlcSettingsPage";

const App = ({history}) => {

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
                discordId: decoded.sub,
                discordAvatar: decoded.avatar,
                isAdmin: decoded.is_admin
            });
        }


        axios.get("/api/cards")
            .then((response) => {
                if (!CardStore.initialised) {
                    CardStore.addCards(response.data);
                    setLoading(false);
                }
            })
            .catch((error) => {
                console.error('Error loading cards', error);
            });

    }, []);

    const logout = () => {
        window.localStorage.clear();
        setLoggedInPlayer(undefined);
        history.push('/');
    }

    if (loading) {
        return <div>Loading...</div>
    }
    return (
        <div>
            <TopLevelMenu loggedInPlayer={loggedInPlayer}/>

            <Switch>
                <Route exact path="/">
                    <HomePage />
                </Route>
                <Route exact path="/collection">
                    <PlayerCollection loggedInPlayer={loggedInPlayer}/>
                </Route>
                <Route exact path="/pack-shop">
                    <PackShopPage loggedInPlayer={loggedInPlayer}/>
                </Route>
                <Route path="/packs">
                    <PackOpener/>
                </Route>
                <Route path="/modtester">
                    <ModTester/>
                </Route>
                <Route exact path="/matches">
                    <MatchesPage loggedInPlayer={loggedInPlayer} />
                </Route>
                <Route path="/matches/:matchId">
                    <MatchPage loggedInPlayer={loggedInPlayer} />
                </Route>
                <Route exact path='/dlc-settings'>
                    <DlcSettingsPage loggedInPlayer={loggedInPlayer} />
                </Route>
                <Route path="/admin">
                    <AdminPage />
                </Route>
            </Switch>

            <Footer onLogout={logout} loggedInPlayer={loggedInPlayer} />
        </div>
    );
}

export default withRouter(App);
