import React, { Component } from 'react';
import AppHeader from './../header/AppHeader';
import Sidebar from './../left-sidebar/Sidebar';
import Content from './../mainContainer/MainContent';
import AppFooter from './../footer/AppFooter';
import './App.css';

class App extends Component {
  constructor(){
    super()
    this.state = ({job: {jobId: 2}});
    this.handleJobClick = this.handleJobClick.bind(this);
  }

  handleJobClick(activeJob){
    var tmpJob = {jobId: {activeJob}};
    this.setState( {job: {jobId: activeJob}} );
  }

  render() {
    return (
      <div className="wrapper">
        <AppHeader />
      	<div className="middle">
          <Content job={this.state.job}/>
          <Sidebar onClick={this.handleJobClick}/>
      	</div>
        <AppFooter />
      </div>
    );
  }
}

export default App;
