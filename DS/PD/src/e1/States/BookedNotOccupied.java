package e1.States;

import e1.Hotel.Room;

public class BookedNotOccupied implements Status {

	private static final BookedNotOccupied estado = new BookedNotOccupied();
	private BookedNotOccupied(){}
	public static BookedNotOccupied getStatus() {
		return estado;
	}

	@Override
	public void bookRoom(Room room, String client) {
		throw new IllegalStateException("Room is already booked");
	}

	@Override
	public void cancelBooking(Room room) {
		room.setState(CleanApproved.getStatus());
		room.setClient(null);
	}

	@Override
	public void checkIn(Room room, String client) {
		if (!room.getClient().equals(client))
			throw new IllegalStateException("This client doesn't have reservation for this room");
		room.setState(Occupied.getStatus());
	}

	@Override
	public void cleanRoom(Room room, String cleanStaff) {
		throw new IllegalStateException("Cannot clean a room once it's booked");
	}

	@Override
	public void freeRoom(Room room) {
		throw new IllegalStateException("Can't free a room before the client checks-in");
	}

	@Override
	public void approveClean(Room room, boolean approve) {
		throw new IllegalStateException("Room is not currently under cleaning");
	}

	@Override
	public String stringState(Room room) {
		return "Booked by " + room.getClient() + ". Not occupied\n";
	}
}
