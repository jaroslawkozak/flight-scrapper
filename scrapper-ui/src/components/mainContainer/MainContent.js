import React, { Component } from 'react';
import JobFlightDataTable from './../scrapper-data/flightDataTable/JobFlightDataTable';
import FlightDetails from './../scrapper-data/flightDataTable/flightDetails';
import {hostsconfig} from '../../properties/config.js'
import axios from 'axios'
import './MainContent.css';

class Content extends Component {
  constructor(){
    super();
    this.handleFlightEntryClick = this.handleFlightEntryClick.bind(this);
    this.state = { 
      activeJob: 0,
      displayEntryDetails: false,
      loadingDetails: false,
      currentFlightEntry: [],
      outboundFlightDetails: [],
      inboundFlightDetails: [],
      flightData: [],
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
  
      this.setState({ loadingDetails : true }, () => {
        axios.get(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/flights/getSingleFlightDetails?flightId=" + flightEntry.outboundFlightId)
        .then(response => this.setState( {outboundFlightDetails : response.data }))
        .catch(error => console.log(error.response));

        axios.get(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/flights/getSingleFlightDetails?flightId=" + flightEntry.inboundFlightId)
        .then(response => this.setState( {inboundFlightDetails : response.data, loadingDetails : false}))
        .catch(error => console.log(error.response));
      });

    }
  }

  getJobData(jobId){
    this.setState({ loading: true }, () => {
      axios.get(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/flights/getOneMonthData?jobId=" + jobId)
        .then(response => this.setState({flightData: response.data, loading: false}))
        .catch(error => console.log(error.response));
    });
  }

  toggleBool(bool){
    return bool ? false : true;
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.activeJob !== this.state.activeJob) {
      this.setState({ activeJob: nextProps.activeJob });
      this.getJobData(nextProps.activeJob);
    }
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
              flightData={this.state.flightData} 
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
