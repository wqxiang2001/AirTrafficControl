
import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import AirCraftList from './AirCraftList';
import AirCraftEnqueue from './AirCraftEnqueue';


class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path='/' exact={true} component={Home}/>
          <Route path='/aircrafts' exact={true} component={AirCraftList}/>
          <Route path='/aircrafts/enqueue' component={AirCraftEnqueue}/>
        </Switch>
      </Router>
    )
  }
}

export default App;
