import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

import 'react-dropdown/style.css';

class AirCraftEnqueue extends Component {
  emptyItem = {
    type: 'Emergency',
    size: 'SMALL'
  };

  constructor(props) {
    super(props);
    this.state = {
      item: this.emptyItem
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }


  handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    let item = {...this.state.item};
    item[name] = value;
    this.setState({item});
  }
 
  async handleSubmit(event) {
    event.preventDefault();

    //let item = {...this.state.item};
    //item[type] = 
    const {item} = this.state;

    await fetch('/aircraft/enqueue', {
      method:'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item),
    });
    this.props.history.push('/aircrafts');
  }

  render() {
    const {item} = this.state;
    const title = <h2>Enqueue A New Air Craft</h2>;
    
    return <div>
      <AppNavbar/>
      <Container>
        {title}
        <Form onSubmit={this.handleSubmit}>
          <FormGroup>
            <Label for="type">Craft Type : </Label>
            
            <select name="type" id="type" value={item.type || ''} onChange={this.handleChange}>
                <option value="Emergency">Emergency</option>
                <option value="VIP">VIP</option>
                <option value="Passenger">Passenger</option>
                <option value="Cargo">Cargo</option>
            </select>
          </FormGroup>
          <FormGroup>
            <Label for="size">Craft Size :  </Label>
            <select name="size" id="size" value={item.size || ''} onChange={this.handleChange}>
                <option value="SMALL">SMALL</option>
                <option value="LARGE">LARGE</option>
            </select>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit">Enqueue</Button>{' '}
            <Button color="secondary" tag={Link} to="/aircrafts">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  }
}

export default withRouter(AirCraftEnqueue);