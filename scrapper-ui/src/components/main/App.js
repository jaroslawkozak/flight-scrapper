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
        flightData: []
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
          <Content flightData={this.state.flightData}/>
          <Sidebar onClick={this.handleJobClick}/>
      	</div>
        <AppFooter />
      </div>
    );
  }

  getJobData(jobId){
    axios.get("http://127.0.0.1:7701/getJobData?jobId=" + jobId)
      .then(response => this.setState({flightData: response.data}))
      .catch(error => console.log(error.response));
  }

}
export default App;
