import React, { Component } from 'react';
import axios from 'axios'

class JobCalendar extends Component {
  render() {
    return (
      <li className="calendar">
        <div>
          {this.props.job.jobId}
        </div>
      </li>
    );
  }

  getJobs(){
    axios.get('http://127.0.0.1:7701/getJobs')
      .then(response => this.setState({jobs: response.data}))
      .catch(error => console.log(error.response));
  }

}
export default JobCalendar;
