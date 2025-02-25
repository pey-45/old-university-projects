package e1.States;

import e1.Hotel.Room;

public interface Status {
     void bookRoom(Room room, String client);          //Reservar habitación
     void cancelBooking(Room room);                    //Terminar reserva
     void checkIn(Room room, String client);           //Ocupar habitación
     void cleanRoom(Room room, String cleanStaff);     //Limpiar habitación
     void freeRoom(Room room);                         //Liberar habitación
     void approveClean(Room room, boolean approve);    //Aprobar limpieza
     String stringState(Room room);

}
