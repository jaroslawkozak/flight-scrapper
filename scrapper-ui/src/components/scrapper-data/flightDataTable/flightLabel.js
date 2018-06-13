import React, { Component } from 'react';
import './flightDataTable.css';

class FlightLabel extends Component {

  render() {
    const { label }  = this.props;
    var tmpLabel = label.substring(5);
    var month = tmpLabel.substring(0, 2);
    var day = tmpLabel.substring(3, 5);
    return ( 
      <div className="flightLabel">
        {day + " / " + month}
      </div>
    );
  }

}
export default FlightLabel;
