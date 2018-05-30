import React, { Component } from 'react';
import JobFlightDataTable from './../scrapper-data/flightDataTable/JobFlightDataTable';
import './MainContent.css';

class Content extends Component {
  render() {
    return (
      <div className="container">
        <main className="contentWrapper">
          <div className="content">
            <JobFlightDataTable flightData={this.props.flightData} />
          </div>
        </main>
      </div>
    );
  }
}

export default Content;
