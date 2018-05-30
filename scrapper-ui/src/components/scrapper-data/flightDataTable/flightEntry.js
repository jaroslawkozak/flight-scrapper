import React, { Component } from 'react';
import './flightEntry.css'

class FlightEntry extends Component {

  render() {
    const { flightData }  = this.props;
    return (
      <div className="flightEntry">
          {/*outId: {flightData.outboundFlightId}
          inId: {flightData.inboundFlightId}*/}
          {flightData.inboundFlightDate} <br /> {flightData.days} - {flightData.price}
          {/*inDate: {flightData.inboundFlightDate}*/}
      </div>
    );
  }

}
export default FlightEntry;
