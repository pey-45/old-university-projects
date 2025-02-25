package e1;

import e1.Hotel.*;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HotelTest {

	static Hotel testHotel;
	static String supervisor1, supervisor2, supervisor3,
			      cleanStaff1, cleanStaff2, cleanStaff3;

	@BeforeEach
	void setup()
	{
		testHotel = Hotel.getHotel_only_instance();
		supervisor1 = "La portera";
		supervisor2 = "Manolo el moroso";
		supervisor3 = "Don Hurón";
		cleanStaff1 = "El tendero Don Senén";
		cleanStaff2 = "El ladrón Ceferino";
		cleanStaff3 = "La anciana amante de los animales";

		testHotel.addSupervisor(supervisor1);
		testHotel.addSupervisor(supervisor2);
		testHotel.addSupervisor(supervisor3);

		testHotel.addCleanStaff(cleanStaff1);
		testHotel.addCleanStaff(cleanStaff2);
		testHotel.addCleanStaff(cleanStaff3);

		testHotel.addRoom(supervisor1);
		testHotel.addRoom(supervisor2);
		testHotel.addRoom(supervisor3);
		testHotel.addRoom(supervisor1);
		testHotel.addRoom(supervisor2);
		testHotel.addRoom(supervisor3);
		testHotel.addRoom(supervisor1);
	}

	@Test
	void testExceptions() {
		//Asignar supervisor que no existe a una habitación
		assertThrows(IllegalArgumentException.class, () -> testHotel.addRoom("Bob"));

		//Limpiar una habitación con un CleanStaff que no existe
		testHotel.checkIn(1, "BobClient");
		testHotel.freeRoom(1);
		assertThrows(IllegalArgumentException.class, () -> testHotel.cleanRoom(1, "BobDonLimpio"));

		//Un supervisor que no está asignado a la habitación aprueba la limpieza
		testHotel.cleanRoom(1, cleanStaff1);
		assertThrows(IllegalArgumentException.class, () -> testHotel.approveCleaning(1, supervisor2, true));

		//Un supervisor que no está asignado a la habitación desaprueba la limpieza
		testHotel.approveCleaning(1, supervisor1, true);
		assertThrows(IllegalArgumentException.class, () -> testHotel.approveCleaning(1, supervisor2, false));

		assertThrows(IllegalArgumentException.class, () -> testHotel.addSupervisor("La portera")); //Añadir supervisor que ya existe
		assertThrows(IllegalArgumentException.class, () -> testHotel.removeSupervisor("Bob")); //Borrar un supervisor que no existe
		assertThrows(IllegalArgumentException.class, () -> testHotel.addCleanStaff("El tendero Don Senén")); //Añadir un CleanStaff que ya existe
		assertThrows(IllegalArgumentException.class, () -> testHotel.removeCleanStaff("Bob")); //Borrar un cleanStaff que no existe
		assertThrows(IllegalArgumentException.class, () -> testHotel.bookRoom(420, "Bob")); //getRoom de una habitación que no existe

		assertThrows(IllegalStateException.class, testHotel::showRoomsInfo); //Mostrar información de hotel antes de ponerle nombre

		//Darle nombre a un hotel que ya tenía nombre previamente
		testHotel.setHotelName("Howotel");
		assertThrows(IllegalStateException.class, () -> testHotel.setHotelName("13 Rue del Percebe"));

		testHotel.resetHotel();
	}

	@Test
	void testStatus() {
		assertThrows(IllegalStateException.class, () -> testHotel.cancelBooking(1)); //Cancelar reserva de habitación libre
		assertThrows(IllegalStateException.class, () -> testHotel.cleanRoom(1, cleanStaff1)); //Limpiar habitación ya limpia sin quitarle la aprobación previamente
		assertThrows(IllegalStateException.class, () -> testHotel.freeRoom(1)); //Liberar una habitación que no está ocupada
		assertThrows(IllegalStateException.class, () -> testHotel.approveCleaning(1, supervisor1, true)); //Aprobar limpieza de una habitación aprobada

		//ESTADO BOOKED-NOT-OCCUPIED
		testHotel.bookRoom(1, "Bob");
		assertThrows(IllegalStateException.class, () -> testHotel.bookRoom(1, "BobSus"));//Reservar habitación ya reservada
		assertThrows(IllegalStateException.class, () -> testHotel.checkIn(1, "BobSus")); //CheckIn de una persona que no es cliente que reservó la habitación
		assertThrows(IllegalStateException.class, () -> testHotel.cleanRoom(1, cleanStaff1)); //Limpiar una habitación después de reservada
		assertThrows(IllegalStateException.class, () -> testHotel.freeRoom(1)); //Liberar una habitación antes de ocuparla
		assertThrows(IllegalStateException.class, () -> testHotel.approveCleaning(1, supervisor1, false)); //Desaprobar limpieza de habitación ya reservada

		//ESTADO OCCUPIED
		testHotel.checkIn(1, "Bob");
		assertThrows(IllegalStateException.class, () -> testHotel.bookRoom(1, "BobSus")); //Reservar habitación ocupada
		assertThrows(IllegalStateException.class, () -> testHotel.cancelBooking(1)); //Cancelar reserva tras ocupar habitación
		assertThrows(IllegalStateException.class, () -> testHotel.checkIn(1, "Bob")); //Check-In después de hacer el check-in previamente
		assertThrows(IllegalStateException.class, () -> testHotel.cleanRoom(1, cleanStaff1)); //Limpiar habitación tras ocupar habitación
		assertThrows(IllegalStateException.class, () -> testHotel.approveCleaning(1, supervisor1, true)); //Aprobar limpieza de habitación ocupada
		assertThrows(IllegalStateException.class, () -> testHotel.approveCleaning(1, supervisor1, false)); //Desaprobar limpieza de habitación ocupada

		//ESTADO FREE-WAITING-CLEAN
		testHotel.freeRoom(1);
		assertThrows(IllegalStateException.class, () -> testHotel.bookRoom(1, "DirtyDan")); //Reservar habitación antes de limpiar y aprobarla
		assertThrows(IllegalStateException.class, () -> testHotel.cancelBooking(1)); //Cancelar reserva de habitación esperando a limpiarse
		assertThrows(IllegalStateException.class, () -> testHotel.checkIn(1, "DirtyDan")); //Check-In de habitación esperando a limpiarse
		assertThrows(IllegalStateException.class, () -> testHotel.freeRoom(1)); //Liberar habitación que acaba de ser liberada
		assertThrows(IllegalStateException.class, () -> testHotel.approveCleaning(1, supervisor1, true)); //Aprobar limpieza antes de limpiar
		assertThrows(IllegalStateException.class, () -> testHotel.approveCleaning(1, supervisor1, false)); //Desaprobar limpieza antes de limpiar

		//ESTADO CLEAN-WAITING-APPROVAL
		testHotel.cleanRoom(1, cleanStaff1);
		assertThrows(IllegalStateException.class, () -> testHotel.bookRoom(1, "BobAgain"));//Reservar habitación que no está confirmada limpia
		assertThrows(IllegalStateException.class, () -> testHotel.cancelBooking(1)); //Cancelar reserva de habitación que no está confirmada limpia
		assertThrows(IllegalStateException.class, () -> testHotel.checkIn(1, "BobAgain")); //Check-In de habitación que no está confirmada lista
		assertThrows(IllegalStateException.class, () -> testHotel.cleanRoom(1, cleanStaff1)); //CleanRoom de una habitación esperando aprobación
		assertThrows(IllegalStateException.class, () -> testHotel.freeRoom(1)); //Liberar habitación que no está aprobada

		testHotel.resetHotel();
	}

	@Test
	void testGeneral() {
		testHotel.bookRoom(1, "BobTheBooker"); //Room 1 queda como reservada pero no ocupada

		testHotel.bookRoom(2, "BobTheCanceler"); //Room 2 se reserva y se cancela, queda como libre aprobada
		testHotel.cancelBooking(2);

		testHotel.checkIn(3, "BobTheCheckerIn"); //Room 3 queda como ocupada

		testHotel.checkIn(4, "BobFromTheRoomToClean"); //Room 4 queda como libre pendiente de limpieza
		testHotel.freeRoom(4);

		testHotel.checkIn(5, "BobFromTheCleanedRoom"); //Room 5 queda limpia pendiente de aprobación
		testHotel.freeRoom(5);
		testHotel.cleanRoom(5, cleanStaff1);

		testHotel.checkIn(6, "BobFromTheApprovedRoom"); //Room 6 queda limpia aprobada
		testHotel.freeRoom(6);
		testHotel.cleanRoom(6, cleanStaff2);
		testHotel.approveCleaning(6, supervisor3, true);

		testHotel.checkIn(7, "BobFromTheDisapprovedRoom"); //Room 7 queda como libre pendiente de limpieza
		testHotel.freeRoom(7);
		testHotel.cleanRoom(7, cleanStaff3);
		testHotel.approveCleaning(7, supervisor1, false);

		testHotel.setHotelName("Howotel");
		assertEquals(testHotel.showRoomsInfo(), """
				***********************
				Hotel Howotel
				***********************
				Room no. 1: Booked by BobTheBooker. Not occupied
				Room no. 2: Free. This room was approved by Manolo el moroso
				Room no. 3: Booked by BobTheCheckerIn. Occupied
				Room no. 4: Free. Cleaning pending
				Room no. 5: Free. Room cleaned by El tendero Don Senén, pending approval
				Room no. 6: Free. This room was approved by Don Hurón
				Room no. 7: Free. Cleaning pending
				""");

		ArrayList<Room> availableRooms = new ArrayList<>();
		availableRooms.add(testHotel.getRoom(2));
		availableRooms.add(testHotel.getRoom(6));
		assertEquals(availableRooms, testHotel.getAvailableRooms());

		ArrayList<Room> roomsToClean = new ArrayList<>();
		roomsToClean.add((testHotel.getRoom(4)));
		roomsToClean.add(testHotel.getRoom(7));
		assertEquals(roomsToClean, testHotel.getRoomsToClean());

		ArrayList<Room> roomsWaitingApproval = new ArrayList<>();
		roomsWaitingApproval.add(testHotel.getRoom(5));
		assertEquals(roomsWaitingApproval, testHotel.getRoomsWaitingApproval());

		testHotel.removeSupervisor(supervisor1);
		testHotel.removeCleanStaff(cleanStaff1);

		testHotel.resetHotel();
	}
}
