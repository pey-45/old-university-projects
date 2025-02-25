package es.udc.ws.app.model.appservice.exceptions;

public class EmailAlreadyRegisteredException extends Exception {

    private Long courseId;
    private String email;

    public EmailAlreadyRegisteredException(Long courseId, String email) {
        super("Email " + email + " is already registered in course with id " + courseId);
        this.courseId = courseId;
        this.email = email;
    }

    public Long getCourseId() {
        return courseId;
    }
    public String getEmail() {
        return email;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
