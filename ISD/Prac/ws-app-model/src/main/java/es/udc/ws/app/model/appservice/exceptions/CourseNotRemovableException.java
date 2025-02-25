package es.udc.ws.app.model.appservice.exceptions;

public class CourseNotRemovableException extends Exception {
    private Long courseId;

    public CourseNotRemovableException(Long courseId) {
        super("Course with id " + courseId + " cannot be deleted because it has registrations");
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
