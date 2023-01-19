import React from "react";
import ReactDOM from 'react-dom';
import {HashRouter, Route, Switch} from "react-router-dom";
import Home from "./home";
import Survey from "./Survey";
import ConsentForm from "./ConsentForm";

ReactDOM.render(
    <HashRouter>
      <Switch>
        <Route exact path="/" component={Home}/>
        <Route path="/survey/:id" component={Survey}/>
        <Route path="/consent/:id" component={ConsentForm}/>
      </Switch>
    </HashRouter>,
    document.getElementById('react'));
