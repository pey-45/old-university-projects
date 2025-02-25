package e1.States;


import e1.Hotel.Room;

public class Occupied implements Status {

    private static final Occupied estado = new Occupied();
    private Occupied(){}
    public static Occupied getStatus() {
        return estado;
    }

    @Override
    public void bookRoom(Room room, String client) {
        throw new IllegalStateException("Room is already occupied");
    }

    @Override
    public void cancelBooking(Room room) {
        throw new IllegalStateException("Can't cancel a room after occupying ");
    }

    @Override
    public void checkIn(Room room, String client) {
        throw new IllegalStateException("The client of this room already checked-in ");
    }

    @Override
    public void cleanRoom(Room room, String cleanStaff) {
        throw new IllegalStateException("Can't clean room while it is occupied");
    }

    @Override
    public void freeRoom(Room room) {
        room.setState(FreeWaitingClean.getStatus());
        room.setClient(null);
    }

    @Override
    public void approveClean(Room room, boolean approve) {
        throw new IllegalStateException("Room is not currently under cleaning");
    }



    @Override
    public String stringState(Room room) {
        return "Booked by " + room.getClient() + ". Occupied\n";
    }
}
