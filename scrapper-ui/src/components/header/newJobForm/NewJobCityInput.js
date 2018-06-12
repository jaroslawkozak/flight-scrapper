import React, { Component } from 'react';
import './NewJobForm.css';

class NewJobCityInput extends Component {

  render() {
    return (
      <div className="station">
        <input type="text" name={this.props.stationType} id={this.props.stationType} className="stationInput" onChange={this.props.onChange}/>
        <label className="stationLabel">{this.props.stationType.toUpperCase()}</label>
      </div>
    );
  }
}

export default NewJobCityInput;