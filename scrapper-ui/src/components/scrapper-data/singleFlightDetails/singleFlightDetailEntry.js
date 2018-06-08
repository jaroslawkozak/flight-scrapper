import React, { Component } from 'react';
import WizzairLogo from '../../../images/airlinesLogo/Wizzair.png'
import './singleFlightDetailEntry.css'

class singleFlightDetailEntry extends Component {
  
  getSingleFlightContent(){
    const { flightDetails }  = this.props;
    return (
      <div className="singleFlightContent">
        <div className="city">
          {flightDetails.departureAirport.city.toUpperCase()} ( {flightDetails.flightData.departureStationIATA} )
        </div>
        <div className="hours">
          {flightDetails.flightData.departureDates}
        </div>
        <div className="priceWrapper">
          <div className="price">
            {flightDetails.flightData.amount}
          </div>
          <div className="currency">
            {flightDetails.currency.currencyCode}
          </div>
        </div>
        <img className="airlineName" src={WizzairLogo}/> <br />
      </div>
    )
  }

  render() {
    return (
      <div className="singleFlightDetails">
        {this.getSingleFlightContent()}
      </div>
    )
  }

}
export default singleFlightDetailEntry;
