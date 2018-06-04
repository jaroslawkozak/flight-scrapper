import React, { Component } from 'react';
import './flightDataTable.css'

class FlightEntry extends Component {

  render() {
    const { flightData }  = this.props;
    if(typeof flightData != 'undefined'){
      return ( 
        <div className="flightEntry">
         {flightData.days} - {flightData.price}
        </div>
      );
    } else {
      return ( 
        <div>
         
        </div>
      );
    }
  }

}
export default FlightEntry;
