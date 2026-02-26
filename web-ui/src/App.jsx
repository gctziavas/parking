import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar.jsx';
import Home from './pages/Home.jsx';
import FindParking from './pages/FindParking.jsx';
import MyBookings from './pages/MyBookings.jsx';
import OwnerDashboard from './pages/OwnerDashboard.jsx';
import AdminPanel from './pages/AdminPanel.jsx';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <div className="app">
        <Navbar user="demo@spacedrop.com" onLogout={() => {}} />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/parking" element={<FindParking />} />
            <Route path="/bookings" element={<MyBookings />} />
            <Route path="/owner" element={<OwnerDashboard />} />
            <Route path="/admin" element={<AdminPanel />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
