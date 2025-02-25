package es.udc.ws.app.model.registration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Registration {

    private Long registrationId;
    private Long courseId;
    private String email;
    private String cardNumber;
    private LocalDateTime registrationDate;
    private LocalDateTime cancellationDate;


    // No id, no cancellation
    public Registration(Long courseId, String email, String cardNumber, LocalDateTime registrationDate) {

        this.courseId = courseId;
        this.email = email;
        this.cardNumber = cardNumber;
        this.registrationDate = registrationDate != null? registrationDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }

    // Id, no cancellation
    public Registration(Long registrationId, Long courseId, String email, String cardNumber, LocalDateTime registrationDate) {
        this(courseId, email, cardNumber, registrationDate);
        this.registrationId = registrationId;
    }

    // No id, cancellation
    public Registration(Long courseId, String email, String cardNumber, LocalDateTime registrationDate, LocalDateTime cancellationDate) {
        this(courseId, email, cardNumber, registrationDate);
        this.cancellationDate = cancellationDate != null? cancellationDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }

    // Id, cancellation
    public Registration(Long registrationId, Long courseId, String email, String cardNumber, LocalDateTime registrationDate, LocalDateTime cancellationDate) {
        this(courseId, email, cardNumber, registrationDate);
        this.registrationId = registrationId;
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
        return cardNumber;
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
    public int hashCode() {
        final int prime = 31;
        int result;
        result = prime + ((registrationId == null)? 0 : registrationId.hashCode());
        result = prime * result + (courseId == null ? 0 : courseId.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
        result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
        result = prime * result + ((cancellationDate == null) ? 0 : cancellationDate.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Registration other = (Registration) obj;

        if (registrationId == null) {
            if (other.registrationId != null) {
                return false;
            }
        } else if (!registrationId.equals(other.registrationId)) {
            return false;
        }

        if (courseId == null) {
            if (other.courseId != null) {
                return false;
            }
        } else if (!courseId.equals(other.courseId)) {
            return false;
        }

        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }

        if (cardNumber == null) {
            if (other.cardNumber != null) {
                return false;
            }
        } else if (!cardNumber.equals(other.cardNumber)) {
            return false;
        }

        if (registrationDate == null) {
            if (other.registrationDate != null) {
                return false;
            }
        } else if (!registrationDate.equals(other.registrationDate)){
             return false;
        }

        if (cancellationDate == null) {
            return other.cancellationDate == null;
        } else {
            return cancellationDate.equals(other.cancellationDate);
        }
    }
}
