package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDateTime;

public class RegistrationOutOfDeadlineException extends Exception {
    private Long courseId;
    private LocalDateTime startDate;

    public RegistrationOutOfDeadlineException(Long courseId, LocalDateTime startDate) {
        super("The deadline for registering for course id " + courseId + " has passed. Course start date: " + startDate);
        this.courseId = courseId;
        this.startDate = startDate;
    }


    public Long getCourseId() {
        return courseId;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

}
