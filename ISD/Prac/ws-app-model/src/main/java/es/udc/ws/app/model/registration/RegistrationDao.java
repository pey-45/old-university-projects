package es.udc.ws.app.model.registration;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.util.List;

public interface RegistrationDao {
    Registration create(Connection connection, Registration registration);
    Registration find(Connection connection, Long registrationId) throws InstanceNotFoundException;
    List<Registration> findBy(Connection connection, String email);
    void update(Connection connection, Registration registration) throws InstanceNotFoundException;
    void remove(Connection connection, Long registrationId) throws InstanceNotFoundException;
}
