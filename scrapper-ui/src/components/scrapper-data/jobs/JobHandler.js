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
    this.handleJobDelete = this.handleJobDelete.bind(this);
    this.handleToggleClick = this.handleToggleClick.bind(this);
  }

  render(){
    return(
      this.state.loading ? <LoadingSpinner /> :
      this.state.jobs.map(function(item){
        return <Job
                job={item}
                onClick={this.handleJobClick}
                onToggle={this.handleToggleClick}
                onDelete={this.handleJobDelete}
                key={"job_" + item.jobId}
              />;
      }, this)
    );
  }

  handleJobClick(jobId){
    this.props.onClick(jobId);
  }

  handleJobDelete(jobId){
    this.deleteJob(jobId);
  }

  handleToggleClick(jobId, isChecked){
    isChecked ? this.deactivateJob(jobId) : this.activateJob(jobId);
  }

  activateJob(jobId){
    axios.post(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/jobs/activate?jobId=" + jobId)
      .then(() => { this.getJobs(); this.render();})
      .catch(error => console.log(error.response));
  }

  deactivateJob(jobId){
    axios.post(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/jobs/deactivate?jobId=" + jobId)
      .then(() => { this.getJobs(); this.render();})
      .catch(error => console.log(error.response));
  }

  deleteJob(jobId){
    axios.post(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/jobs/delete?jobId=" + jobId)
      .then(() => { this.getJobs(); this.render();})
      .catch(error => console.log(error.response));
  }

  getJobs(){
    axios.get(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/jobs/get")
      .then(response => {this.setState({jobs: response.data, loading: false}); this.render()})
      .catch(error => console.log(error.response));
  }

  componentDidMount() {
    var intervalId = setInterval(this.getJobs.bind(this), 10000);
    this.setState({intervalId: intervalId});
  }
  componentWillUnmount() {
    clearInterval(this.state.intervalId);
  }
}
export default JobsComponent
