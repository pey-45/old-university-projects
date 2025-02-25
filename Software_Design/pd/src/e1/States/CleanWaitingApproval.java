package e1.States;

import e1.Hotel.Room;

public class CleanWaitingApproval implements Status {

    private static final CleanWaitingApproval estado = new CleanWaitingApproval();
    private CleanWaitingApproval(){}
    public static CleanWaitingApproval getStatus() {
        return estado;
    }

    @Override
    public void bookRoom(Room room, String client) {
        throw new IllegalStateException("Can't book a room without previous cleaning approval");
    }

    @Override
    public void cancelBooking(Room room) {
        throw new IllegalStateException("Room isn't currently booked");
    }

    @Override
    public void checkIn(Room room, String client) {
        throw new IllegalStateException("Can't book a room without previous cleaning approval");
    }

    @Override
    public void cleanRoom(Room room, String setCleanStaff) {
        throw new IllegalStateException("Room was already cleaned and is waiting for approval");
    }

    @Override
    public void freeRoom(Room room) {
        throw new IllegalStateException("Room isn't currently occupied");
    }

    @Override
    public void approveClean(Room room, boolean approve) {
        if(approve)
            room.setState(CleanApproved.getStatus());
        else
            room.setState(FreeWaitingClean.getStatus());
    }

    @Override
    public String stringState(Room room) {
        return "Free. Room cleaned by " + room.getCleanStaff() + ", pending approval\n";
    }
}

