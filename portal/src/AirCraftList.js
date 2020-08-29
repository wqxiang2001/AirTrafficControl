import React, { Component } from 'react';
import { Button, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';



class AirCraftList extends Component {

    constructor(props) {
      super(props);
      this.state = {aircrafts: [], isLoading: true};
      this.dequeue = this.dequeue.bind(this);
    }
  
    componentDidMount() {
      this.setState({isLoading: true});
  
      fetch('/aircraft/list')
        .then(response => response.json())
        .then(data => this.setState({aircrafts: data, isLoading: false}));
    }
  
    dequeue() {
      fetch(`/aircraft/dequeue`)
        .then(response => response.json())
        .then(data => {
          let updatedAirCrafts = [...this.state.aircrafts].filter(i => i.id !== data.id);
          this.setState({aircrafts: updatedAirCrafts});
        });
    }
  
    render() {
      const {aircrafts, isLoading} = this.state;
  
      if (isLoading) {
        return <p>Loading...</p>;
      }
  
      const airCraftList = aircrafts.map(aircraft => {
        
        return <tr key={aircraft.id}>
          <td>{aircraft.id}</td>
          <td style={{whiteSpace: 'nowrap'}}>{aircraft.type}</td>
          <td>{aircraft.size}</td>
          
        </tr>
      });
  
      return (
        <div>
          <AppNavbar/>
          <Container fluid>
            <div className="float-right">
              <Button color="success" tag={Link} to="/aircrafts/enqueue">Enqueue A New Air Craft</Button>

              <Button color="success" onClick={() => this.dequeue()}>Dequeue A Air Craft</Button>
          
            </div>
            <h3>Air Craft List</h3>
            <Table className="mt-4">
              <thead>
              <tr>
                <th width="20%">type</th>
                <th width="40%">type</th>
                <th width="40%">size</th>
              </tr>
              </thead>
              <tbody>
              {airCraftList}
              </tbody>
            </Table>
          </Container>
        </div>
      );
    }
  }
  
  export default AirCraftList;