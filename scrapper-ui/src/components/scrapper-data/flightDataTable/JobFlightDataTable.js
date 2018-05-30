import React, { Component } from 'react';
import FlightEntry from './flightEntry'

class JobFlightDataTable extends Component {
  createTable = () => {
    let table = [];
    let rows = 20;
    let cols = 20;
    // Outer loop to create parent
    for (let i = 0; i < cols; i++) {
      let children = []
      //Inner loop to create children
      for (let j = 0; j < rows; j++) {
        children.push(<td>{`000`}&nbsp;</td>)
      }
      //Create the parent and add the children
      table.push(<tr>{children}</tr>)
    }
    return table
  }

  getDataEntries = () => {
    let flightData = this.props.flightData;
    let rows = [];
    for(var i=0; i < flightData.length; i++) {

      if(flightData[i].inboundFlightDate in rows){
        rows[flightData[i].inboundFlightDate].push(<FlightEntry flightData={flightData[i]}/>);
      } else {
        rows[flightData[i].inboundFlightDate] = [];
        rows[flightData[i].inboundFlightDate].push(<FlightEntry flightData={flightData[i]}/>);
      }
    }
    console.log(rows);
    return null;
    {/*
    let flightData = this.props.flightData;
    let rows = [];
    for(var i=0; i < flightData.length; i++) {
      if(flightData[i].outboundFlightDate in rows){
        rows[flightData[i].outboundFlightDate].push(<FlightEntry flightData={flightData[i]}/>);
      } else {
        rows[flightData[i].outboundFlightDate] = [];
        rows[flightData[i].outboundFlightDate].push(<FlightEntry flightData={flightData[i]}/>);
      }
    }
    
    let resultData = []
    for(var key in rows){
      resultData.push(<div><div className="flightEntry">{key}</div>{rows[key]}</div>);
    }
  return resultData;*/}
  }

  render() {
    return (
       this.getDataEntries() 
    );
  }
}
export default JobFlightDataTable;
