package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.course.Course;
import es.udc.ws.app.model.registration.Registration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class RegistrationRestDto {
    private Long registrationId;
    private Long courseId;
    private String email;
    private String cardNumber;
    private LocalDateTime registrationDate;
    private LocalDateTime cancellationDate;

    public RegistrationRestDto() {
    }

    public RegistrationRestDto(Long registrationId, Long courseId, String email, String cardNumber, LocalDateTime registrationDate, LocalDateTime cancellationDate) {
        this.registrationId = registrationId;
        this.courseId = courseId;
        this.email = email;
        this.cardNumber = cardNumber;
        this.registrationDate = registrationDate != null? registrationDate.truncatedTo(ChronoUnit.SECONDS) : null;
        this.cancellationDate = cancellationDate != null? cancellationDate.truncatedTo(ChronoUnit.SECONDS) : null;
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
        return cardNumber.substring(cardNumber.length() - 4);
    }
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    public LocalDateTime getCancellationDate() {
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
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate != null? registrationDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }
    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = registrationDate != null? cancellationDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }

    @Override
    public String toString() {
        String trimmedCardNumber = getCardNumber();
        String registrationDateStr = registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String cancellationDateStr = (cancellationDate != null) ? cancellationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "Not canceled";

        return String.format(
                "RegistrationDto [registrationId=%d, courseId=%d, email=%s, cardNumber=************%s, registrationDate=%s, cancellationDate=%s]",
                registrationId, courseId, email, trimmedCardNumber, registrationDateStr, cancellationDateStr
        );
    }
}
