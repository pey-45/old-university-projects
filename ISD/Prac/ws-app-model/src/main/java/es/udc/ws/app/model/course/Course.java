package es.udc.ws.app.model.course;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Course {

    private Long courseId;
    private String city;
    private String name;
    private LocalDateTime startDate;
    private double price;
    private int maxPlaces;
    private LocalDateTime registrationDate;
    private int availablePlaces;

    // No courseId specified
    public Course(String city, String name, LocalDateTime startDate, double price, int maxPlaces) {
        this.city = city;
        this.name = name;
        this.startDate = startDate != null? startDate.truncatedTo(ChronoUnit.SECONDS) : null;
        this.price = price;
        this.maxPlaces = maxPlaces;
        this.availablePlaces = maxPlaces;
    }

    // courseId specified
    public Course(Long courseId, String city, String name, LocalDateTime startDate, double price, int maxPlaces) {
        this(city, name, startDate, price, maxPlaces);
        this.courseId = courseId;
    }

    // Registration date specified
    public Course(Long courseId, String city, String name, LocalDateTime startDate, double price, int maxPlaces, LocalDateTime registrationDate) {
        this(courseId, city, name, startDate, price, maxPlaces);
        this.registrationDate = registrationDate != null? registrationDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }

    public Course(Long courseId, String city, String name, LocalDateTime startDate, double price, int maxPlaces, LocalDateTime registrationDate, int availablePlaces) {
        this(courseId, city, name, startDate, price, maxPlaces, registrationDate);
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
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
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
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate != null? registrationDate.truncatedTo(ChronoUnit.SECONDS) : null;
    }
    public void setAvailablePlaces(int availablePlaces) {
        this.availablePlaces = availablePlaces;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result;
        result = prime + ((courseId == null) ? 0 : courseId.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + (int) Double.doubleToLongBits(price);
        result = prime * result + maxPlaces;
        result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
        result = prime * result + availablePlaces;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Course other = (Course) obj;

        if (courseId == null) {
            if (other.courseId != null)
                return false;
        } else if (!courseId.equals(other.courseId))
            return false;

        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;

        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;

        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;

        if (Double.compare(price, other.price) != 0)
            return false;

        if (maxPlaces != other.maxPlaces)
            return false;

        if (registrationDate == null) {
            if (other.registrationDate != null)
                return false;
        } else if (!registrationDate.equals(other.registrationDate))
            return false;

        return availablePlaces == other.availablePlaces;
    }
}