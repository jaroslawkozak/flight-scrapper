import React, { Component } from 'react';
import './toggleButton.css'

class ToggleButton extends Component { 
    render() {
        var toggleId = "cmn-toggle-" + this.props.id;
        var isChecked = (this.props.isChecked === 1) ? true : false;
        return (
            <div className="switch">
                <input id={toggleId} className="cmn-toggle cmn-toggle-round" type="checkbox" defaultChecked={isChecked} onClick={this.props.toggleClick}/>
                <label htmlFor={toggleId}></label>
            </div>
        );
    }
}
export default ToggleButton;