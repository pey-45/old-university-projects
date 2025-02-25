package es.udc.ws.app.client.service.dto;

public class ClientRegistrationDto {
    private Long registrationId;
    private Long courseId;
    private String email;
    private String cardNumber;
    private String registrationDate;
    private String cancellationDate;

    public ClientRegistrationDto(Long registrationId, Long courseId, String email, String cardNumber, String registrationDate, String cancellationDate) {
        this.registrationId = registrationId;
        this.courseId = courseId;
        this.email = email;
        this.cardNumber = cardNumber;
        this.registrationDate = registrationDate;
        this.cancellationDate = cancellationDate;
    }

    public Long getRegistrationId() {
        return registrationId;
    }
    public Long getCourseId() {
        return courseId;
    }
    public String getEmail() {
        return email;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public String getRegistrationDate() {
        return registrationDate;
    }
    public String getCancellationDate() {
        return cancellationDate;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    @Override
    public String toString() {
        /*return String.format(
                "ClientRegistrationDto [registrationId=%d, courseId=%d, email=%s, cardNumber=********%s, registrationDate=%s, cancellationDate=%s]",
                registrationId, courseId, email, cardNumber, registrationDate,
                (cancellationDate != null ? cancellationDate : "Not canceled")
        );*/
        return String.format("""
                Registration {
                    Id: %d,
                    Course Id: %d,
                    Email: %s,
                    Card Number: ************%s,
                    Registration Date: %s,
                    Cancellation Date: %s
                }""",
                registrationId, courseId, email, cardNumber, registrationDate,
                (cancellationDate != null ? cancellationDate : "Not cancelled")
        );
    }
}
