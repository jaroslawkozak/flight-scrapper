import React, { Component } from 'react';
import axios from 'axios'
import Job from './Job';

class JobsComponent extends Component {
  constructor(props){
    super(props);
    this.state = { jobs: [] };
    this.getJobs();
    this.handleJobClick = this.handleJobClick.bind(this);
  }

  render(){
    return(
      this.state.jobs.map(function(item){
        return <Job
                jobId={item.jobId}
                departureStationIATA={item.departureStationIATA}
                arrivalStationIATA={item.arrivalStationIATA}
                isActive={item.isActive}
                onClick={this.handleJobClick}
              />;
      }, this)
    );
  }

  handleJobClick(jobId){
    this.props.onClick(jobId);
  }

  getJobs(){
    axios.get('http://127.0.0.1:7701/getJobs')
      .then(response => this.setState({jobs: response.data}))
      .catch(error => console.log(error.response));
  }

}
export default JobsComponent
