import React, { Component } from 'react';
import './flightDataTable.css'

class FlightEntry extends Component {
  constructor() {
    super();
    this.flightClick = this.flightClick.bind(this);
  }

  flightClick(){
    this.props.onClick(this.props.flightData);
  }

  render() {
    const { flightData, fill }  = this.props;

    if(typeof flightData !== 'undefined'){
      let red = 255 * fill;
      let blue = 255 - 255 * fill;
      let styles = {
        background: 'rgba(' + Math.round(red) + ',' + Math.round(blue) + ',0,0.2)'
      };

      return ( 
        <div className="flightEntry" style={styles} onClick={this.flightClick}>
         {Math.round(flightData.price)} | {flightData.days}
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
