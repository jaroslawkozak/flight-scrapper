import React, { Component } from 'react';
import './Job.css'

class Job extends Component {
  constructor(props) {
    super(props);
    this.jobClick = this.jobClick.bind(this);
    this.state = { click: 0 };
  }

  jobClick(){
    this.setState({ click: this.state.click + 1 });
    this.props.onClick(this.props.jobId);
  }

  render() {
    const { jobId, departureStationIATA, arrivalStationIATA, isActive }  = this.props;
    const classes = (isActive === 1) ? "jobItem" : "jobItem inactive";
    return (
      <li key={jobId} className={classes} onClick={this.jobClick}>
        <div>
          {departureStationIATA} - {arrivalStationIATA} - {this.state.click} - {this.props.jobId}
        </div>
      </li>
    );
  }
}
export default Job;
