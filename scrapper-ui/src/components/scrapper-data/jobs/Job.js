import React, { Component } from 'react';
import ToggleButton from '../../toggleButton/toggleButton.js'
import deleteImage from '../../../images/status/delete.svg'
import errorImage from '../../../images/status/error.svg'
import warningIdleImage from '../../../images/status/warning.svg'
import okImage from '../../../images/status/ok.svg'
import './Job.css'

class Job extends Component {
  constructor(props) {
    super(props);
    this.state = { click: 0 };
    this.jobClick = this.jobClick.bind(this);
    this.jobDelete = this.jobDelete.bind(this);
    this.toggleClick = this.toggleClick.bind(this);
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


  getClassIfIdle(baseClass){
    return this.isJobIdle() ? baseClass + " idle" : baseClass;
  }

  isJobIdle(){
    var HALF_HOUR = 30*60*1000;
    return new Date() - new Date(this.props.job.lastSuccessfull) > HALF_HOUR;
  }

  getStatusImage(){
    if(this.props.job.isActive === 1) {
        if(this.props.job.status === "failing"){
          return (<div className="tooltip">
                    <img className="statusImage" src={ errorImage } alt="Job Status"/> 
                    <span className="tooltiptext">Job is active, but there is problem with fetching flight data by scrapper. Maybe flight connection doesn't exist.</span>
                  </div>)
        } else if(this.isJobIdle()) {
          return (<div className="tooltip">
                    <img className="statusImage" src={ warningIdleImage } alt="Job Status"/>
                    <span className="tooltiptext">Job is active, but there were no new report for more than 30 minutes. Either no scrapper is working or you have to wait 10 minutes.</span>
                  </div>
          )
        } else {
          return (<div className="tooltip">
                    <img className="statusImage" src={ okImage } alt="Job Status"/> 
                    <span className="tooltiptext">Job is running ok.</span>
                  </div>)
        }
      
    } else {
      return "";
    }
  }


  render() {
    const { jobId, departureStationIATA, arrivalStationIATA, lastSuccessfull }  = this.props.job;
    const classes = (this.props.job.isActive === 1) ? this.getClassIfIdle("jobItem") : "jobItem inactive";
    return (
        <li key={jobId} className={classes} >
          <div className="jobDetails" onClick={this.jobClick}>
            {departureStationIATA} - {arrivalStationIATA} 
            <p className="lastSuccess">{lastSuccessfull}</p>
          </div>
          <div className="tooltip">
            <img className="deleteImage" src={ deleteImage } alt="Delete Job" onClick={this.jobDelete} />
            <span className="tooltiptext">Removes a job.</span>
          </div>
          
          <ToggleButton id={jobId} isChecked={this.props.job.isActive} toggleClick={this.toggleClick}/>
          {this.getStatusImage()}         
        </li>       
    );
  }
}
export default Job;
