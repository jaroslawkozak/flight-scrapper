import React, { Component } from 'react';
import './Sidebar.css';
import JobHandler from '../scrapper-data/jobs/JobHandler';

class Sidebar extends Component {
  constructor(props){
    super(props);
    this.handleJobClick = this.handleJobClick.bind(this);
  }

  handleJobClick(jobId){
    this.props.onClick(jobId);
  }

  render() {
    return (
      <aside className="left-sidebar">
        <strong>Jobs:</strong>
        <JobHandler onClick={this.handleJobClick}/>
      </aside>
    );
  }

}

export default Sidebar;
