import { useState } from 'react';

export default function FindParking() {
  const [searchQuery, setSearchQuery] = useState('');

  const sampleLots = [
    { id: 1, name: 'Downtown Garage', address: '123 Main St', availableSpots: 15, totalSpots: 50, hourlyRate: 5.00 },
    { id: 2, name: 'Airport Parking', address: '456 Airport Rd', availableSpots: 120, totalSpots: 200, hourlyRate: 8.00 },
    { id: 3, name: 'Mall Parking', address: '789 Shopping Ave', availableSpots: 0, totalSpots: 100, hourlyRate: 3.00 },
  ];

  const filtered = sampleLots.filter(lot =>
    lot.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    lot.address.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div className="page">
      <h2>Find Parking</h2>
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search by name or address..."
          value={searchQuery}
          onChange={e => setSearchQuery(e.target.value)}
          className="search-input"
        />
      </div>
      <div className="parking-grid">
        {filtered.map(lot => (
          <div key={lot.id} className="parking-card">
            <h3>{lot.name}</h3>
            <p className="address">{lot.address}</p>
            <div className="parking-details">
              <span className={`availability ${lot.availableSpots > 0 ? 'available' : 'full'}`}>
                {lot.availableSpots > 0 ? `${lot.availableSpots} spots available` : 'Full'}
              </span>
              <span className="rate">${lot.hourlyRate.toFixed(2)}/hr</span>
            </div>
            <button
              className="btn btn-primary"
              disabled={lot.availableSpots === 0}
            >
              {lot.availableSpots > 0 ? 'Book Now' : 'No Spots'}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
