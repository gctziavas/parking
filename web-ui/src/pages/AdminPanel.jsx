export default function AdminPanel() {
  const platformStats = {
    totalUsers: 1250,
    totalOwners: 48,
    totalLots: 156,
    totalBookings: 8420,
    revenue: 42100.00,
  };

  const recentUsers = [
    { id: 1, name: 'John Doe', email: 'john@example.com', role: 'USER', status: 'Active' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'OWNER', status: 'Active' },
    { id: 3, name: 'Bob Wilson', email: 'bob@example.com', role: 'USER', status: 'Active' },
  ];

  return (
    <div className="page">
      <h2>Admin Panel</h2>
      <div className="dashboard-stats">
        <div className="stat-card">
          <h4>Total Users</h4>
          <span className="stat-value">{platformStats.totalUsers}</span>
        </div>
        <div className="stat-card">
          <h4>Parking Owners</h4>
          <span className="stat-value">{platformStats.totalOwners}</span>
        </div>
        <div className="stat-card">
          <h4>Parking Lots</h4>
          <span className="stat-value">{platformStats.totalLots}</span>
        </div>
        <div className="stat-card">
          <h4>Total Bookings</h4>
          <span className="stat-value">{platformStats.totalBookings}</span>
        </div>
        <div className="stat-card">
          <h4>Total Revenue</h4>
          <span className="stat-value">${platformStats.revenue.toLocaleString()}</span>
        </div>
      </div>
      <h3>Recent Users</h3>
      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {recentUsers.map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td><span className="role-badge">{user.role}</span></td>
                <td><span className="status-badge status-confirmed">{user.status}</span></td>
                <td>
                  <button className="btn btn-small btn-outline">View</button>
                  <button className="btn btn-small btn-danger" style={{ marginLeft: '0.5rem' }}>Disable</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
