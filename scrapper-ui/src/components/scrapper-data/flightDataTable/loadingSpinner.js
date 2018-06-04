import React, { Component } from 'react';
import spinnerImage from './loading_icon.gif'
import './flightDataTable.css'

class LoadingSpinner extends Component {
    render() {
        return (
        <div className="spinner">
            <img className="spinner-image" src={ spinnerImage } /> <br />
            Loading...
        </div>
        );
    }
}
export default LoadingSpinner;