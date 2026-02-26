export default function Home() {
  return (
    <div className="page">
      <div className="hero">
        <h1>Welcome to SpaceDrop</h1>
        <p className="hero-subtitle">Find and book parking spots in your city</p>
      </div>
      <div className="features">
        <div className="feature-card">
          <h3>🔍 Find Parking</h3>
          <p>Search for available parking spots near you with real-time availability.</p>
        </div>
        <div className="feature-card">
          <h3>📅 Book Instantly</h3>
          <p>Reserve your spot in seconds and pay securely through the platform.</p>
        </div>
        <div className="feature-card">
          <h3>🏢 For Owners</h3>
          <p>List your parking lots and manage bookings from a dedicated dashboard.</p>
        </div>
      </div>
    </div>
  );
}
