package es.udc.ws.app.client.service.exceptions;

public class CancellationEmailDoesNotMatchException extends Exception{

    Long registrationId;
    String email;

    public CancellationEmailDoesNotMatchException(Long registrationId, String email) {
        super("Cannot cancel registration with id " + registrationId + " because email " + email + " does not match with registration email");

        this.registrationId = registrationId;
        this.email = email;
    }

    public Long getRegistrationId() {
        return registrationId;
    }
    public String getEmail() {
        return email;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
