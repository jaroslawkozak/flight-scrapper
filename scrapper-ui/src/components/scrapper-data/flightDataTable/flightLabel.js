import React, { Component } from 'react';
import './flightDataTable.css';

class FlightLabel extends Component {

  render() {
    const { label }  = this.props;
    return ( 
      <div className="flightLabel">
        {label}
      </div>
    );
  }

}
export default FlightLabel;
