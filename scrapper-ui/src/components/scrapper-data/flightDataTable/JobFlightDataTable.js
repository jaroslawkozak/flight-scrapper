import React, { Component } from 'react';
import FlightEntry from './flightEntry';
import FlightLabel from './flightLabel';
import LoadingSpinner from './loadingSpinner';
import * as _ from 'lodash';

class JobFlightDataTable extends Component {
  getDataEntries = () => {
    let flightData = this.props.flightData;
    let cols = [];
    let rows = [];

    this.getUniqueColumnsAndRows(flightData, cols, rows);
    let resultData = this.getResultDataWithEmptyEntries(cols, rows);
    this.fillResultDataWithFligthData(resultData, flightData);
   
    let tableContent = [];
    tableContent.push(<tr>{this.fillAndGetTableHeader(cols)}</tr>);

    for(var rowIter = 0; rowIter < rows.length; rowIter++){
      tableContent.push(<tr>{this.fillAndGetTableRow(resultData, cols, rows[rowIter])}</tr>);
    }

    let table = <table><tbody>{tableContent}</tbody></table>
    return table;
  }

  getUniqueColumnsAndRows(flightData, cols, rows){
    for(var i=0; i < flightData.length; i++) {
      if(!cols.includes(flightData[i].inboundFlightDate)){
        cols.push(flightData[i].inboundFlightDate);
      }

      if(!rows.includes(flightData[i].outboundFlightDate)){
        rows.push(flightData[i].outboundFlightDate);
      } 
    }
    cols.sort();
    rows.sort();
  }

  fillResultDataWithFligthData(resultData, flightData){
    for(var i=0; i < flightData.length; i++) {
      resultData[flightData[i].inboundFlightDate][flightData[i].outboundFlightDate] = <td><FlightEntry flightData={flightData[i]}/></td>;
    }
  }

  getResultDataWithEmptyEntries(cols, rows){
    let resultData = []
    for(var columnIter = 0; columnIter < cols.length; columnIter++){
      resultData[cols[columnIter]] = [];
      for(var rowIter = 0; rowIter < rows.length; rowIter++){
        resultData[cols[columnIter]][rows[rowIter]] = <td><FlightEntry/></td>;
      }
    }
    return resultData;
  }

  fillAndGetTableRow(resultData, cols, rowLabel){
    let tableRow = [];
      tableRow.push(<td><FlightLabel label={rowLabel}/></td>);
      for(var columnIter = 0; columnIter < cols.length; columnIter++){
        tableRow.push(resultData[cols[columnIter]][rowLabel])
      }
    return tableRow;
  }

  fillAndGetTableHeader(cols){
    let tableHeaderRow = [];
    tableHeaderRow.push(<td></td>);
    for(var columnIter = 0; columnIter < cols.length; columnIter++){
      tableHeaderRow.push(<td><FlightLabel label={cols[columnIter]}/></td>)
    }
    return tableHeaderRow;
  }

  render() {
    console.log('loading', this.props.loading);
    return (
        this.props.loading ? <LoadingSpinner /> : this.getDataEntries()
    );
  }
}
export default JobFlightDataTable;
