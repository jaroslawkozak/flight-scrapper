import React, { Component } from 'react';
import NewJobForm from './newJobForm/NewJobForm.js'
import './AppHeader.css';

class AppHeader extends Component {
  render() {
    return (
      <div className="header">
        <div className="headerTitle">
          Flight-scrapper
        </div>
        <NewJobForm />
      </div>
    );
  }
}

export default AppHeader;
