import React, { Component } from 'react';
import AppHeader from '../components/AppHeader';
import ContactsList from '../components/ContactsList';

class App extends Component {
  render() {
    return (
      <div>
        <AppHeader />
        <main className="ui main text container">
          <ContactsList />
        </main>
      </div>
    );
  }
}

export default App;
