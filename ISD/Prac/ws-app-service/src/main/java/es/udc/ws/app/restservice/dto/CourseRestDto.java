package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CourseRestDto {
    private Long courseId;
    private String city;
    private String name;
    private LocalDateTime startDate;
    private double price;
    private int maxPlaces;
    private int availablePlaces;

    public CourseRestDto() {
    }

    public CourseRestDto(Long courseId, String city, String name, LocalDateTime startDate, double price, int maxPlaces, int availablePlaces) {
        this.courseId = courseId;
        this.city = city;
        this.name = name;
        this.startDate = startDate != null ? startDate.truncatedTo(ChronoUnit.SECONDS) : null;
        this.price = price;
        this.maxPlaces = maxPlaces;
        this.availablePlaces = availablePlaces;
    }

    // Getters
    public Long getCourseId() {
        return courseId;
    }
    public String getCity() {
        return city;
    }
    public String getName() {
        return name;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public double getPrice() {
        return price;
    }
    public int getMaxPlaces() {
        return maxPlaces;
    }
    public int getAvailablePlaces() {
        return availablePlaces;
    }

    // Setters
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate != null? startDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setMaxPlaces(int maxPlaces) {
        this.maxPlaces = maxPlaces;
    }
    public void setAvailablePlaces(int availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

    @Override
    public String toString() {
        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
                "CourseDto [courseId=%d, city=%s, name=%s, startDate=%s, price=%.2f, maxPlaces=%d, availablePlaces=%d]",
                courseId, city, name, startDateStr, price, maxPlaces, availablePlaces
        );
    }
}