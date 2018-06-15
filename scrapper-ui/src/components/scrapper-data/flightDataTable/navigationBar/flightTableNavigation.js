import React, { Component } from 'react';
import DatePicker from 'react-date-picker';
import './navigationBar.css'

class FlightTableNavigation extends Component {
  constructor() {
    super();
    this.handleNewDate = this.handleNewDate.bind(this);
    this.state = {
      date: new Date(),
    }
  }

  onCalendarChange = date => this.setState({ date });
                              
  handleNewDate() {
    this.props.onNewDate(this.state.date);
  }
                              
  render() {
    return (   
      <div className='navigationWrapper'>
        <form className="navigationFormContainer">
          <div className="from">
            <label className="fromLabel">FROM</label>
            <DatePicker calendarClassName="calendar" value={this.state.date} onChange={this.onCalendarChange}/>
          </div>
          <div className="showButton" onClick={this.handleNewDate} > Show </div>
        </form>
      </div>   
    )
  }

}
export default FlightTableNavigation;