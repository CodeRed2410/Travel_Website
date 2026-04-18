package com.example.travel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "travel_bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "TRAVEL_BOOKING_SEQ", allocationSize = 1)
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false, name = "travel_date")
    private LocalDate travelDate;

    @Column(nullable = false)
    private Integer guests;

    @Column(name = "special_requests")
    private String specialRequests;

    @Column(nullable = false, name = "user_email")
    private String userEmail;

    @Column(nullable = false, name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "booking_status")
    private String bookingStatus = "CONFIRMED";

    public Booking() {
        this.bookingDate = LocalDate.now();
    }

    public Booking(String destination, LocalDate travelDate, Integer guests, String specialRequests, String userEmail) {
        this();
        this.destination = destination;
        this.travelDate = travelDate;
        this.guests = guests;
        this.specialRequests = specialRequests;
        this.userEmail = userEmail;
    }

    // Getters and Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }

    public Integer getGuests() { return guests; }
    public void setGuests(Integer guests) { this.guests = guests; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
}
