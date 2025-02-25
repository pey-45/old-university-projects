package e1.Hotel;

import e1.States.CleanApproved;
import e1.States.CleanWaitingApproval;
import e1.States.FreeWaitingClean;

import java.util.ArrayList;

public class Hotel
{
    private String name = null;
    private final ArrayList<Room> room_list = new ArrayList<>();
    private final ArrayList<String> supervisor_list = new ArrayList<>();
    private final ArrayList<String> cleanStaff_list = new ArrayList<>();
    private int room_cnt = 1;

    //Componentes Patrón Instancia Única (Singleton)
    private static final Hotel hotel_only_instance = new Hotel();
    private Hotel(){}
    public static Hotel getHotel_only_instance() {return hotel_only_instance;}

    //Añadir habitación
    public void addRoom(String supervisor) {
        if (!personExists(supervisor_list, supervisor))
            throw new IllegalArgumentException("Supervisor not found");
        Room room = new Room();
        room.setSupervisor(supervisor);
        room.setRoomNum(room_cnt++);
        room_list.add(room);
    }

    //Reservar habitación
    public void bookRoom(int roomNum, String client) {
        hotel_only_instance.getRoom(roomNum).bookRoom(client);
    }

    //Terminar reserva
    public void cancelBooking(int roomNum) {
        hotel_only_instance.getRoom(roomNum).cancelBooking();
    }

    //Check-in (no está en enunciado)
    public void checkIn(int roomNum, String client) {
        hotel_only_instance.getRoom(roomNum).checkIn(client);
    }

    //Limpiar habitación
    public void cleanRoom(int roomNum, String cleanStaff) {
        if(!personExists(cleanStaff_list, cleanStaff))
            throw new IllegalArgumentException("Clean staff member not found");

        hotel_only_instance.getRoom(roomNum).cleanRoom(cleanStaff);
    }

    //Liberar habitación
    public void freeRoom(int roomNum) {
        hotel_only_instance.getRoom(roomNum).freeRoom();
    }

    //Aprobar limpieza
    public void approveCleaning(int roomNum, String supervisor, boolean approve) {
        if (!getRoom(roomNum).getSupervisor().equals(supervisor))
            throw new IllegalArgumentException("This supervisor is not assigned to this room");

        hotel_only_instance.getRoom(roomNum).approveCleaning(approve);
    }

    //Habitaciones disponibles
    public ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room currentRoom : hotel_only_instance.room_list)
            if (currentRoom.getStatus() instanceof CleanApproved)
                availableRooms.add(currentRoom);

        return availableRooms;
    }

    //Habitaciones pendientes de limpieza
    public ArrayList<Room> getRoomsToClean() {
        ArrayList<Room> roomsToClean = new ArrayList<>();

        for (Room currentRoom : hotel_only_instance.room_list)
            if (currentRoom.getStatus() instanceof FreeWaitingClean)
                roomsToClean.add(currentRoom);

        return roomsToClean;
    }

    //Habitaciones pendientes de aprobación
    public ArrayList<Room> getRoomsWaitingApproval() {
        ArrayList<Room> roomsWaitingApproval = new ArrayList<>();

        for (Room currentRoom : hotel_only_instance.room_list)
            if (currentRoom.getStatus() instanceof CleanWaitingApproval)
                roomsWaitingApproval.add(currentRoom);

        return roomsWaitingApproval;
    }

    //Información de las habitaciones del hotel
    public String showRoomsInfo() {
        if (hotel_only_instance.name == null)
            throw new IllegalStateException("Choose Hotel name before showing its information");

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***********************\nHotel ").append(hotel_only_instance.getHotelName()).append("\n***********************\n");

        for (Room currentRoom : hotel_only_instance.room_list)
            stringBuilder.append("Room no. ").append(currentRoom.getRoomNum()).append(": ").append(currentRoom.stringState());

        return stringBuilder.toString();
    }

    public void addSupervisor(String name) {
        if (personExists(supervisor_list, name))
            throw new IllegalArgumentException(name + " already is a supervisor");
        supervisor_list.add(name);
    }

    public void removeSupervisor(String name) {
        if (!personExists(supervisor_list, name))
            throw new IllegalArgumentException(name + " is not a supervisor");
        supervisor_list.remove(name);
    }

    public void addCleanStaff(String name) {
        if (personExists(cleanStaff_list, name))
            throw new IllegalArgumentException(name + " already is a member of the clean staff");
        cleanStaff_list.add(name);
    }

    public void removeCleanStaff(String name) {
        if (!personExists(cleanStaff_list, name))
            throw new IllegalArgumentException(name + " is not member of the clean staff");
        cleanStaff_list.remove(name);
    }

    public void setHotelName(String name) {
        if (hotel_only_instance.name != null)
            throw new IllegalStateException("The hotel already has a name selected");
        hotel_only_instance.name = name;
    }

    public String getHotelName() {
        return hotel_only_instance.name;
    }

    private boolean personExists(ArrayList<String> list, String name) {
        if (list.isEmpty())
            return false;

        for (String person: list)
            if (person.equals(name))
                return true;

        return false;
    }

    public Room getRoom(int roomNum){
        for (Room currentRoom : room_list)
            if (currentRoom.getRoomNum() == roomNum)
                return currentRoom;

        throw new IllegalArgumentException("Room not found. Make sure the selected number is between 1 and " + room_cnt);
    }

    //Función para los tests
    public void resetHotel() {
        this.name = null;
        this.room_cnt = 1;
        this.room_list.clear();
        this.cleanStaff_list.clear();
        this.supervisor_list.clear();
    }
}
