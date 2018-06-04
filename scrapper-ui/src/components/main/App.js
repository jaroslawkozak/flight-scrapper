import React, { Component } from 'react';
import AppHeader from './../header/AppHeader';
import Sidebar from './../left-sidebar/Sidebar';
import Content from './../mainContainer/MainContent';
import AppFooter from './../footer/AppFooter';
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
    var DATA_MANAGER_HOST_ADDRESS = "http://10.22.90.79"
    //var DATA_MANAGER_HOST_ADDRESS = "http://localhost"
    var DATA_MANAGER_HOST_PORT = "7701";
    this.setState({ loading: true }, () => {
      axios.get(DATA_MANAGER_HOST_ADDRESS + ":" + DATA_MANAGER_HOST_PORT + "/getJobData?jobId=" + jobId)
        .then(response => this.setState({flightData: response.data, loading: false}))
        .catch(error => console.log(error.response));
    });
  }

}
export default App;
