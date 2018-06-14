import React, { Component } from 'react';
import AppHeader from './../header/AppHeader';
import Sidebar from './../left-sidebar/Sidebar';
import Content from './../mainContainer/MainContent';
import AppFooter from './../footer/AppFooter';
import './App.css';

class App extends Component {
  constructor(){
    super()
    this.state = ({
        job: {jobId: 0},
        loading: false
      });
    this.handleJobClick = this.handleJobClick.bind(this);
  }

  handleJobClick(activeJob){
    this.setState( {job: {jobId: activeJob}} );
    this.setState( {flightData: []} )
    {/*this.getJobData(activeJob);*/}
  }

  render() {
    return (
      <div className="wrapper">
        <AppHeader />
      	<div className="middle">
          <Content activeJob={this.state.job.jobId} loading={this.state.loading}/>
          <Sidebar onClick={this.handleJobClick}/>
      	</div>
        <AppFooter />
      </div>
    );
  }



}
export default App;
