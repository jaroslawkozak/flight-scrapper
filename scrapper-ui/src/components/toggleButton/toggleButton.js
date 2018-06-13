import React, { Component } from 'react';
import './toggleButton.css'

class ToggleButton extends Component { 
    constructor(props) {
        super(props); 
        this.state = { isChecked: (this.props.isChecked === 1) ? true : false }      
      }

    render() {
        var toggleId = "cmn-toggle-" + this.props.id;
        return (
            <div className="switch">
                <input id={toggleId} className="cmn-toggle cmn-toggle-round" type="checkbox" defaultChecked={this.state.isChecked} onClick={this.props.toggleClick}/>
                <label htmlFor={toggleId}></label>
            </div>
        );
    }
}
export default ToggleButton;