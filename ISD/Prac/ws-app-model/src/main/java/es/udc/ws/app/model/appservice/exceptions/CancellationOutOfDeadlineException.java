package es.udc.ws.app.model.appservice.exceptions;

public class CancellationOutOfDeadlineException extends Exception {

    Long registrationId;

    public CancellationOutOfDeadlineException(Long registrationId) {
        super("The deadline for cancelling the registration with id " + registrationId + " has passed");
        this.registrationId = registrationId;
    }

    public Long getRegistrationId() {
        return registrationId;
    }


    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }
}
