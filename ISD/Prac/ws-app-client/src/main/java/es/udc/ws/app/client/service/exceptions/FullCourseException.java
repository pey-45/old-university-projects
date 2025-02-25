package es.udc.ws.app.client.service.exceptions;

public class FullCourseException extends Exception {

    private Long courseId;

    public FullCourseException(Long courseId) {
        super("Cannot register for course with id " + courseId + " because it is full");
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
