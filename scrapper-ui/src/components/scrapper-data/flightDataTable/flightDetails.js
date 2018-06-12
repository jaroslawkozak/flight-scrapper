import React, { Component } from 'react';
import SingleFlightDetailsEntry from './../singleFlightDetails/singleFlightDetailEntry';
import LoadingSpinner from '../../loadingSpinner/loadingSpinner.js';
import './flightDataTable.css'

class FlightDetails extends Component {
  
  getSpinner(){
    if(this.props.loading) { return <LoadingSpinner /> ;
    } else { return "";}
  }

  render() {
    const { outboundFlightDetails, inboundFlightDetails }  = this.props;
    if(typeof outboundFlightDetails.flightData !== 'undefined' && typeof inboundFlightDetails.flightData !== 'undefined') {
      return (
        <div className="flightDetailsWrapper">  
          <SingleFlightDetailsEntry flightDetails={outboundFlightDetails}/>
          <SingleFlightDetailsEntry flightDetails={inboundFlightDetails}/>
        </div>
      );
    } else {
      return (
        <div className="flightDetailsWrapper">
          {this.getSpinner()}
        </div>
      );
    }

  }
}
export default FlightDetails;
