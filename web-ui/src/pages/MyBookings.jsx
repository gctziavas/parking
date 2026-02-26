export default function MyBookings() {
  const sampleBookings = [
    { id: 1, parkingLot: 'Downtown Garage', startTime: '2026-02-26 10:00', endTime: '2026-02-26 14:00', status: 'CONFIRMED', totalPrice: 20.00 },
    { id: 2, parkingLot: 'Airport Parking', startTime: '2026-02-27 08:00', endTime: '2026-02-27 20:00', status: 'PENDING', totalPrice: 96.00 },
    { id: 3, parkingLot: 'Mall Parking', startTime: '2026-02-25 12:00', endTime: '2026-02-25 15:00', status: 'COMPLETED', totalPrice: 9.00 },
  ];

  const statusClass = (status) => {
    switch (status) {
      case 'CONFIRMED': return 'status-confirmed';
      case 'PENDING': return 'status-pending';
      case 'COMPLETED': return 'status-completed';
      case 'CANCELLED': return 'status-cancelled';
      default: return '';
    }
  };

  return (
    <div className="page">
      <h2>My Bookings</h2>
      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Parking Lot</th>
              <th>Start</th>
              <th>End</th>
              <th>Status</th>
              <th>Total</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sampleBookings.map(booking => (
              <tr key={booking.id}>
                <td>{booking.id}</td>
                <td>{booking.parkingLot}</td>
                <td>{booking.startTime}</td>
                <td>{booking.endTime}</td>
                <td><span className={`status-badge ${statusClass(booking.status)}`}>{booking.status}</span></td>
                <td>${booking.totalPrice.toFixed(2)}</td>
                <td>
                  {booking.status === 'PENDING' && (
                    <button className="btn btn-small btn-danger">Cancel</button>
                  )}
                  {booking.status === 'CONFIRMED' && (
                    <button className="btn btn-small btn-outline">View</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
