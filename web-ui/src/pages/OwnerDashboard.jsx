export default function OwnerDashboard() {
  const sampleLots = [
    { id: 1, name: 'Downtown Garage', address: '123 Main St', totalSpots: 50, availableSpots: 15, hourlyRate: 5.00, active: true },
    { id: 2, name: 'Riverside Lot', address: '200 River Rd', totalSpots: 30, availableSpots: 30, hourlyRate: 4.00, active: true },
    { id: 3, name: 'Old Town Parking', address: '55 Heritage Blvd', totalSpots: 20, availableSpots: 0, hourlyRate: 6.00, active: false },
  ];

  return (
    <div className="page">
      <h2>Owner Dashboard</h2>
      <div className="dashboard-stats">
        <div className="stat-card">
          <h4>Total Lots</h4>
          <span className="stat-value">{sampleLots.length}</span>
        </div>
        <div className="stat-card">
          <h4>Active Lots</h4>
          <span className="stat-value">{sampleLots.filter(l => l.active).length}</span>
        </div>
        <div className="stat-card">
          <h4>Total Spots</h4>
          <span className="stat-value">{sampleLots.reduce((sum, l) => sum + l.totalSpots, 0)}</span>
        </div>
        <div className="stat-card">
          <h4>Available Spots</h4>
          <span className="stat-value">{sampleLots.reduce((sum, l) => sum + l.availableSpots, 0)}</span>
        </div>
      </div>
      <h3>Your Parking Lots</h3>
      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Address</th>
              <th>Spots</th>
              <th>Available</th>
              <th>Rate</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sampleLots.map(lot => (
              <tr key={lot.id}>
                <td>{lot.name}</td>
                <td>{lot.address}</td>
                <td>{lot.totalSpots}</td>
                <td>{lot.availableSpots}</td>
                <td>${lot.hourlyRate.toFixed(2)}/hr</td>
                <td>
                  <span className={`status-badge ${lot.active ? 'status-confirmed' : 'status-cancelled'}`}>
                    {lot.active ? 'Active' : 'Inactive'}
                  </span>
                </td>
                <td>
                  <button className="btn btn-small btn-outline">Edit</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <button className="btn btn-primary" style={{ marginTop: '1rem' }}>+ Add Parking Lot</button>
    </div>
  );
}
