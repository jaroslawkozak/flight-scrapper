import React, { Component } from 'react';
import NewJobCityInput from './NewJobCityInput.js';
import AddJobButton from './AddJobButton.js';
import {hostsconfig} from '../../../properties/config.js'
import axios from 'axios'
import './NewJobForm.css';

class NewJobForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      origin: '',
      destination: ''
    }
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event){
    this.setState({
      [event.target.name]: event.target.value
    })
  }

  handleSubmit(event) {
    this.addJobRequest();
  }

  addJobRequest(){
    axios.post(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/jobs/add?departureStation=" + this.state.origin + "&arrivalStation=" + this.state.destination)
      .catch(error => console.log(error.response));
  }
  

  render() {
    return (
      <form className="formContainer" onSubmit={this.handleSubmit}>
          <NewJobCityInput stationType="origin" onChange={this.handleChange}/>
          <NewJobCityInput stationType="destination" onChange={this.handleChange}/>
          <AddJobButton />
      </form>
    );
  }
}

export default NewJobForm;
