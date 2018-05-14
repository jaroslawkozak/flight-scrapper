import React, { Component } from 'react';
import axios from 'axios'

class ContactItem extends Component {
  constructor(){
    super();
    this.state = { response: ["aa"], msg: "empty" };

  }

  render() {
    const { login, name, department }  = this.props;
    const imgUrl = `https://api.adorable.io/avatars/55/${login}.png`;
    return (
      <li className="item">
        <img src={imgUrl} className="ui mini rounded image" />
        <div className="content">
          <h4 className="header">{name}</h4>
          <div className="description">{department}</div>
        </div>
        <p>
          <button onClick={this.onClickHandler.bind(this)} > Kliknij </button>
        </p>
        <p>
          {JSON.stringify(this.state.response)}
        </p>
        <p>
          {this.state.msg}
        </p>
      </li>
    );
  }

  onClickHandler(){
    axios.get('http://127.0.0.1:7701/getJobs')
      .then(jobs => this.setState({response: jobs.data}))
      .catch(error => console.log(error.response));
    this.setState({
      msg: "done"
    })
  }
}

export default ContactItem;
