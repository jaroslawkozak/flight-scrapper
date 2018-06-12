import React, { Component } from 'react';
import JobFlightDataTable from './../scrapper-data/flightDataTable/JobFlightDataTable';
import FlightDetails from './../scrapper-data/flightDataTable/flightDetails';
import axios from 'axios'
import './MainContent.css';

class Content extends Component {
  constructor(){
    super();
    this.handleFlightEntryClick = this.handleFlightEntryClick.bind(this);
    this.state = { 
      displayEntryDetails: false,
      loadingDetails: false,
      currentFlightEntry: [],
      outboundFlightDetails: [],
      inboundFlightDetails: [],
    };
  }
  
  handleFlightEntryClick(flightEntry){
    if(flightEntry === this.state.currentFlightEntry){
      this.setState( { displayEntryDetails : this.toggleBool(this.state.displayEntryDetails) } );
    } else{
      this.setState( { currentFlightEntry : flightEntry,
                       outboundFlightDetails : [],
                       inboundFlightDetails : [],
                       displayEntryDetails : true} );
      var DATA_MANAGER_HOST_ADDRESS = "http://10.22.90.79"
      //var DATA_MANAGER_HOST_ADDRESS = "http://localhost"
      var DATA_MANAGER_HOST_PORT = "7701";
  
      this.setState({ loadingDetails : true }, () => {
        axios.get(DATA_MANAGER_HOST_ADDRESS + ":" + DATA_MANAGER_HOST_PORT + "/getFlightDetails?flightId=" + flightEntry.outboundFlightId)
        .then(response => this.setState( {outboundFlightDetails : response.data }))
        .catch(error => console.log(error.response));

        axios.get(DATA_MANAGER_HOST_ADDRESS + ":" + DATA_MANAGER_HOST_PORT + "/getFlightDetails?flightId=" + flightEntry.inboundFlightId)
        .then(response => this.setState( {inboundFlightDetails : response.data, loadingDetails : false}))
        .catch(error => console.log(error.response));
      });

    }
  }

  toggleBool(bool){
    return bool ? false : true;
  }

  render() {
    return (
      <div className="container">
        <main className="contentWrapper">
          {this.state.displayEntryDetails ? <FlightDetails 
                                                outboundFlightDetails={this.state.outboundFlightDetails} 
                                                inboundFlightDetails={this.state.inboundFlightDetails}
                                                loading={this.state.loadingDetails} /> :""}
          <div className="flightData">
            <JobFlightDataTable 
              flightData={this.props.flightData} 
              loading={this.props.loading} 
              onClick={this.handleFlightEntryClick}
            />
          </div>
        </main>
      </div>
    );
  }
}

export default Content;
