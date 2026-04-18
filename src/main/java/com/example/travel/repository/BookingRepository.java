package com.example.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.travel.model.Booking;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserEmail(String userEmail);
    
    List<Booking> findByDestination(String destination);
    
    List<Booking> findByTravelDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.userEmail = :email AND b.travelDate >= :date")
    List<Booking> findUpcomingBookingsByUser(@Param("email") String email, @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.destination = :destination AND b.travelDate = :date")
    Long countBookingsByDestinationAndDate(@Param("destination") String destination, @Param("date") LocalDate date);
}
