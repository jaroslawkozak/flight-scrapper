import React, { Component } from 'react';
import ToggleButton from '../../toggleButton/toggleButton.js'
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

  toggleClick(){
    console.log('clicked')
  }

  render() {
    const { jobId, departureStationIATA, arrivalStationIATA, isActive }  = this.props;
    const classes = (isActive === 1) ? "jobItem" : "jobItem inactive";
    return (
        <li key={jobId} className={classes} >
          <div className="jobDetails" onClick={this.jobClick}>
            {departureStationIATA} - {arrivalStationIATA} - {this.state.click} - {this.props.jobId}
          </div>
          <ToggleButton id={jobId} isChecked={isActive} toggleClick={this.toggleClick}/>
        </li>       
    );
  }
}
export default Job;
