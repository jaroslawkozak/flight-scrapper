import React, { Component } from 'react';
import spinnerImage from './loading_icon.gif'
import './loadingSpinner.css'

class LoadingSpinner extends Component {
    render() {
        this.sleep(1000)
        return (
        <div className="spinner">
            <img className="spinner-image" src={ spinnerImage } alt="loading spinner"/> <br />
            Loading...
        </div>
        );
    }

    sleep(milliseconds) {
        var start = new Date().getTime();
        for (var i = 0; i < 1e7; i++) {
          if ((new Date().getTime() - start) > milliseconds){
            break;
          }
        }
      }
}
export default LoadingSpinner;