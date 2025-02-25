package e1.States;

import e1.Hotel.Room;

public class FreeWaitingClean implements Status {

    private static final FreeWaitingClean estado = new FreeWaitingClean();
    private FreeWaitingClean(){}
    public static FreeWaitingClean getStatus() {
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
    public void cleanRoom(Room room, String cleanStaff) {
        room.setCleanStaff(cleanStaff);
        room.setState(CleanWaitingApproval.getStatus());
    }

    @Override
    public void freeRoom(Room room) {
        throw new IllegalStateException("Room isn't currently occupied");
    }

    @Override
    public void approveClean(Room room, boolean approve) {
        throw new IllegalStateException("Room is not clean yet");
    }

    @Override
    public String stringState(Room room) {
        return "Free. Cleaning pending\n";
    }
}
