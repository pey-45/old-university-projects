package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

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
