package e1.States;

import e1.Hotel.Room;

public class CleanApproved implements Status {

    private static final CleanApproved estado = new CleanApproved();
    private CleanApproved(){}
    public static CleanApproved getStatus() {
        return estado;
    }

    @Override
    public void bookRoom(Room room, String client) {
        room.setState(BookedNotOccupied.getStatus());
        room.setClient(client);
    }

    @Override
    public void cancelBooking(Room room) {
        throw new IllegalStateException("Can't cancel booking in current state");
    }

    @Override
    public void checkIn(Room room, String client) {
        room.setState(Occupied.getStatus());
        room.setClient(client);
    }

    @Override
    public void cleanRoom(Room room, String cleanStaff) {
        throw new IllegalStateException("Room was already cleaned and approved");
    }

    @Override
    public void freeRoom(Room room) {
        throw new IllegalStateException("Room is currently free");
    }

    @Override
    public void approveClean(Room room, boolean approve) {
        throw new IllegalStateException("Room is currently approved");
    }

    @Override
    public String stringState(Room room) {
        return "Free. This room was approved by " + room.getSupervisor() + "\n";
    }
}
