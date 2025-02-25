package es.udc.ws.app.client.service.exceptions;

public class RegistrationAlreadyCancelledException extends Exception {

    Long registrationId;

    public RegistrationAlreadyCancelledException(Long registrationId) {
        super("Registration " + registrationId + " is already cancelled");

        this.registrationId = registrationId;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }
}
