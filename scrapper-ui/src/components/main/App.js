import React, { Component } from 'react';
import AppHeader from './../header/AppHeader';
import Sidebar from './../left-sidebar/Sidebar';
import Content from './../mainContainer/MainContent';
import AppFooter from './../footer/AppFooter';
import {hostsconfig} from '../../properties/config.js'
import axios from 'axios'
import './App.css';

class App extends Component {
  constructor(){
    super()
    this.state = ({
        job: {jobId: 0},
        flightData: [],
        loading: false
      });
    this.handleJobClick = this.handleJobClick.bind(this);
  }

  handleJobClick(activeJob){
    this.setState( {job: {jobId: activeJob}} );
    this.setState( {flightData: []} )
    this.getJobData(activeJob);
  }

  render() {
    return (
      <div className="wrapper">
        <AppHeader />
      	<div className="middle">
          <Content flightData={this.state.flightData} loading={this.state.loading}/>
          <Sidebar onClick={this.handleJobClick}/>
      	</div>
        <AppFooter />
      </div>
    );
  }

  getJobData(jobId){
    this.setState({ loading: true }, () => {
      axios.get(hostsconfig.datamanager.host + ":" + hostsconfig.datamanager.port + "/flights/getOneMonthData?jobId=" + jobId)
        .then(response => this.setState({flightData: response.data, loading: false}))
        .catch(error => console.log(error.response));
    });
  }

}
export default App;
