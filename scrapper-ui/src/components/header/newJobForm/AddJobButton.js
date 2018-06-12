import React, { Component } from 'react';
import './NewJobForm.css';

class AddJobButton extends Component {
  render() {
    return (
        <input type="submit" className="addButton" value="Add Job"/>
    );
  }
}

export default AddJobButton;