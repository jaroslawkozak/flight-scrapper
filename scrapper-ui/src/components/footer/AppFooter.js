import React, { Component } from 'react';
import './AppFooter.css';

class AppFooter extends Component {
  render() {
    return (
      <footer className="footer">
        <strong>ToDo:</strong> <br />
        Job management <br />
        Flight details <br />
        Moving dates <br />
        adding removed job doesn't undelete.
      </footer>
    );
  }
}

export default AppFooter;
