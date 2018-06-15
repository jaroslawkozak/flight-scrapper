import React, { Component } from 'react';
import FlightEntry from './flightEntry';
import FlightLabel from './flightLabel';
import LoadingSpinner from '../../loadingSpinner/loadingSpinner.js';

class JobFlightDataTable extends Component {
  constructor(){
    super();
    this.handleFlightEntryClick = this.handleFlightEntryClick.bind(this);
  }
  
  handleFlightEntryClick(flightEntry){
    this.props.onClick(flightEntry);
  }


  getDataEntries = () => {
    let flightData = this.props.flightData;
    let data = {
      cols : [], 
      rows : [],
      min : -1,
      max : -1
    }

    this.parseFlightData(flightData, data);
    let resultData = this.getResultDataWithEmptyEntries(data);
    this.fillResultDataWithFligthData(resultData, flightData, data);
   
    let tableContent = [];
    var counter = 1;
    tableContent.push(<tr key={"row_" + counter++}>{this.fillAndGetTableHeader(data.cols)}</tr>);

    for(var rowIter = 0; rowIter < data.rows.length; rowIter++){
      tableContent.push(<tr key={"row_" + counter++}>{this.fillAndGetTableRow(resultData, data.cols, data.rows[rowIter])}</tr>);
    }

    let table = <table><tbody>{tableContent}</tbody></table>
    return table;
  }

  parseFlightData(flightData, data){
    for(var i=0; i < flightData.length; i++) {
      if(!data.cols.includes(flightData[i].inboundFlightDate)){
        data.cols.push(flightData[i].inboundFlightDate);
      }

      if(!data.rows.includes(flightData[i].outboundFlightDate)){
        data.rows.push(flightData[i].outboundFlightDate);
      } 

      if(data.min === -1 || parseInt(data.min, 10) > parseInt(flightData[i].price, 10)){
        data.min = flightData[i].price;
      } 

      if(data.max === -1 || parseInt(data.max, 10) < parseInt(flightData[i].price, 10)){
        data.max = flightData[i].price;
      }
    }
    data.cols.sort();
    data.rows.sort();
  }

  normalize(x, data){
    return (x - data.min) / (data.max - data.min);
  }

  fillResultDataWithFligthData(resultData, flightData, data){
    for(var i=0; i < flightData.length; i++) {
      resultData[flightData[i].inboundFlightDate][flightData[i].outboundFlightDate] = <td><FlightEntry 
        flightData={flightData[i]} 
        fill={this.normalize(flightData[i].price, data)}
        onClick={this.handleFlightEntryClick}
        /></td>;
    }
  }

  getResultDataWithEmptyEntries(data){
    let resultData = []
    for(var columnIter = 0; columnIter < data.cols.length; columnIter++){
      resultData[data.cols[columnIter]] = [];
      for(var rowIter = 0; rowIter < data.rows.length; rowIter++){
        resultData[data.cols[columnIter]][data.rows[rowIter]] = <td><FlightEntry/></td>;
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
    var counter=1;
    tableHeaderRow.push(<td key={counter++}></td>);
    for(var columnIter = 0; columnIter < cols.length; columnIter++){
      tableHeaderRow.push(<td key={counter++}><FlightLabel label={cols[columnIter]}/></td>)
    }
    return tableHeaderRow;
  }

  componentWillReceiveProps(nextProps) {
    this.render();
  }

  render() {
    return (
        this.props.loading ? <LoadingSpinner /> : this.getDataEntries()
    );
  }
}
export default JobFlightDataTable;
