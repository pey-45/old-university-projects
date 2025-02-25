package es.udc.ws.app.client.service.dto;

public class ClientCourseDto {
    private Long courseId;
    private String city;
    private String name;
    private String startDate;
    private double price;
    private int maxPlaces;
    private int availablePlaces;

    // Constructor
    public ClientCourseDto(Long courseId, String city, String name, String startDate, double price, int maxPlaces, int availablePlaces) {
        this.courseId = courseId;
        this.city = city;
        this.name = name;
        this.startDate = startDate;
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
    public String getStartDate() {
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
    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
        return String.format("""
                Course {
                    Id: %d,
                    City: %s,
                    Name: %s,
                    Start Date: %s,
                    Price: %.2f,
                    Places: %d,
                    Occupied Places: %d,
                    Available Places: %d
                }""",
                courseId, city, name, startDate, price, maxPlaces, maxPlaces - availablePlaces, availablePlaces
        );
    }
}
