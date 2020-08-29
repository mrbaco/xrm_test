package model;

import java.util.Calendar;

public class TripData {

    private int id;

    private String title = "";

    private String departure;
    private String destination;

    private Calendar departureTime;
    private Calendar arrivalTime;

    private int travelTime;

    private float price;

    public TripData withId(int id) {
        this.id = id;
        return this;
    }

    public TripData withTitle(String title) {
        this.title = title;
        return this;
    }

    public TripData withDepature(String departure) {
        this.departure = departure;
        return this;
    }

    public TripData withDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public TripData withDepartureTime(Calendar departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public TripData withArrivalTime(Calendar arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public TripData withTravelTime(int travelTime) {
        this.travelTime = travelTime;
        return this;
    }

    public TripData withPrice(float price) {
        this.price = price;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDepature() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public Calendar getDepartureTime() {
        return departureTime;
    }

    public Calendar getArrivalTime() {
        return arrivalTime;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public float getPrice() {
        return price;
    }

}