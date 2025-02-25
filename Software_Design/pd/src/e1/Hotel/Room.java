package e1.Hotel;

import e1.States.*;


public class Room
{
    private int roomNum; //se asigna en Hotel
    private Status status;
    private String supervisor, client, cleanStaff;

    public Room() {
        this.status = CleanApproved.getStatus();
        this.client = null;

        //Hace falta guardarlos para cuando se imprime la información de las habitaciones
        this.supervisor = null;
        this.cleanStaff = null;
    }

    //Reservar habitación
    public void bookRoom(String client) {
        this.status.bookRoom(this, client);
    }

    //Terminar reserva
    public void cancelBooking() {
        this.status.cancelBooking(this);
    }

    //Check-in (no está en enunciado)
    public void checkIn(String client) {
        this.status.checkIn(this, client);
    }

    //Limpiar habitación
    public void cleanRoom(String cleanStaff) {
        this.status.cleanRoom(this, cleanStaff);
    }

    //Liberar habitación
    public void freeRoom() {
        this.status.freeRoom(this);
    }

    //Aprobar limpieza
    public void approveCleaning(boolean approve) {
        this.status.approveClean(this, approve);
    }

    //Auxiliar para showRoomsInfo
    public String stringState() {
        return this.status.stringState(this);
    }

    public void setRoomNum(int room_num) {this.roomNum = room_num;}
    public void setSupervisor(String supervisor){this.supervisor = supervisor;}
    public void setCleanStaff(String cleanStaff){this.cleanStaff = cleanStaff;}
    public void setState(Status status) {this.status = status;}
    public void setClient(String client) {this.client = client;}

    public String getSupervisor(){return this.supervisor;}
    public String getCleanStaff(){return  this.cleanStaff;}
    public String getClient() {return this.client;}
    public Status getStatus(){return this.status;}
    public int getRoomNum() {return roomNum;}
}
