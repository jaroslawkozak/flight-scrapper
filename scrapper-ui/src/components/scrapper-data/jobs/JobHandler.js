import React, { Component } from 'react';
import LoadingSpinner from '../../loadingSpinner/loadingSpinner'
import axios from 'axios'
import {hostsconfig} from '../../../properties/config.js'
import Job from './Job';

class JobsComponent extends Component {
  constructor(props){
    super(props);
    this.state = { jobs: [],
                  loading: true };
    this.getJobs();
    this.handleJobClick = this.handleJobClick.bind(this);
  }

  render(){
    return(
      this.state.loading ? <LoadingSpinner /> :
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

  showJobs(){

  }

  handleJobClick(jobId){
    this.props.onClick(jobId);
  }

  getJobs(){
    axios.get(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/getJobs")
      .then(response => this.setState({jobs: response.data, loading: false}))
      .catch(error => console.log(error.response));
  }

}
export default JobsComponent
