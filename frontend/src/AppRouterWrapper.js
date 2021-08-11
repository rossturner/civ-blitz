import './App.css';
import React from "react";
import {BrowserRouter as Router} from "react-router-dom";
import App from "./App";

const AppRouterWrapper = () => {

    return (
        <Router>
            <App />
        </Router>
    );
}

export default AppRouterWrapper;
