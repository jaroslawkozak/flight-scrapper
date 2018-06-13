import React, { Component } from 'react';
import ToggleButton from '../../toggleButton/toggleButton.js'
import deleteImage from '../../../images/status/delete.svg'
import problemImage from '../../../images/status/error.svg'
import okImage from '../../../images/status/ok.svg'
import './Job.css'

class Job extends Component {
  constructor(props) {
    super(props);
    this.jobClick = this.jobClick.bind(this);
    this.jobDelete = this.jobDelete.bind(this);
    this.toggleClick = this.toggleClick.bind(this);
    this.state = { click: 0 };
  }

  jobClick(){
    this.setState({ click: this.state.click + 1 });
    this.props.onClick(this.props.job.jobId);
  }

  jobDelete(){
    this.props.onDelete(this.props.job.jobId)
  }

  toggleClick(){
    this.props.onToggle(this.props.job.jobId, (this.props.job.isActive === 1) ? true : false);
  };

  render() {
    const { jobId, departureStationIATA, arrivalStationIATA, isActive, lastSuccessfull, status }  = this.props.job;
    const classes = (isActive === 1) ? "jobItem" : "jobItem inactive";
    var statusIcon = (status === "failing") ? problemImage : okImage;
    var statusImage = (isActive === 1) ? <img className="statusImage" src={ statusIcon } alt="Job Status"/> : "";   
    return (
        <li key={jobId} className={classes} >
          <div className="jobDetails" onClick={this.jobClick}>
            {departureStationIATA} - {arrivalStationIATA} 
            <p className="lastSuccess">{lastSuccessfull}</p>
          </div>
          <img className="deleteImage" src={ deleteImage } alt="Delete Job" onClick={this.jobDelete}/>
          <ToggleButton id={jobId} isChecked={isActive} toggleClick={this.toggleClick}/>
          {statusImage}         
        </li>       
    );
  }
}
export default Job;
