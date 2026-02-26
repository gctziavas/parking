import { NavLink } from 'react-router-dom';

export default function Navbar({ user, onLogout }) {
  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <span className="navbar-logo">🅿️</span>
        <span className="navbar-title">SpaceDrop</span>
      </div>
      <div className="navbar-links">
        <NavLink to="/" end>Home</NavLink>
        <NavLink to="/parking">Find Parking</NavLink>
        <NavLink to="/bookings">My Bookings</NavLink>
        <NavLink to="/owner">Owner Dashboard</NavLink>
        <NavLink to="/admin">Admin Panel</NavLink>
      </div>
      <div className="navbar-user">
        {user ? (
          <>
            <span className="navbar-username">{user}</span>
            <button className="btn btn-outline" onClick={onLogout}>Logout</button>
          </>
        ) : (
          <span className="navbar-username">Not logged in</span>
        )}
      </div>
    </nav>
  );
}
