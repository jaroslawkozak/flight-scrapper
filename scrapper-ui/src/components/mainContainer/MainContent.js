import React, { Component } from 'react';
import JobCalendar from './../scrapper-data/JobCalendar';
import './MainContent.css';

class Content extends Component {
  render() {
    return (
      <div className="container">
        <main className="contentWrapper">
          <div className="content">
            <JobCalendar job={this.props.job} />
          </div>
        </main>
      </div>
    );
  }
}

export default Content;
