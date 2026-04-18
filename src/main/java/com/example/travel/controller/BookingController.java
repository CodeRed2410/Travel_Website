package com.example.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.travel.model.Booking;
import com.example.travel.repository.BookingRepository;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/api/bookings/confirm")
    public String confirmBooking(
            @RequestParam String destination,
            @RequestParam String travelDate,
            @RequestParam Integer guests,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String userEmail,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            String sessionEmail = (String) session.getAttribute("userEmail");
            if (sessionEmail == null || sessionEmail.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please login first");
                return "redirect:/register.html#login";
            }

            // Validate input
            if (destination == null || destination.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Destination is required");
                return "redirect:/Booking.html";
            }
            
            if (travelDate == null || travelDate.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Travel date is required");
                return "redirect:/Booking.html";
            }
            
            LocalDate date = LocalDate.parse(travelDate);
            if (date.isBefore(LocalDate.now())) {
                redirectAttributes.addFlashAttribute("error", "Travel date cannot be in the past");
                return "redirect:/Booking.html";
            }
            
            if (guests == null || guests < 1 || guests > 10) {
                redirectAttributes.addFlashAttribute("error", "Number of guests must be between 1 and 10");
                return "redirect:/Booking.html";
            }
            
            userEmail = sessionEmail;
            
            // Create new booking
            Booking newBooking = new Booking(
                destination.trim(),
                date,
                guests,
                notes != null ? notes.trim() : "",
                userEmail
            );
            
            // Calculate basic pricing (you can enhance this logic)
            Double basePrice = calculateBasePrice(destination);
            newBooking.setTotalPrice(basePrice * guests);
            
            // Save booking
            Booking savedBooking = bookingRepository.save(newBooking);
            
            // Add success message
            redirectAttributes.addFlashAttribute("success", 
                "Booking confirmed! Your booking ID is: " + savedBooking.getBookingId());
            redirectAttributes.addFlashAttribute("bookingId", savedBooking.getBookingId());
            
            return "redirect:/booking-confirmation.html?bookingId=" + savedBooking.getBookingId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to confirm booking. Please try again.");
            return "redirect:/Booking.html";
        }
    }
    
    @GetMapping("/my-bookings.html")
    public String myBookings(
                           HttpSession session,
                           org.springframework.ui.Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null || email.trim().isEmpty()) {
            return "redirect:/register.html#login";
        }
        
        List<Booking> bookings = bookingRepository.findByUserEmail(email);
        model.addAttribute("bookings", bookings);
        model.addAttribute("userEmail", email);
        
        return "my-bookings";
    }

    @GetMapping("/api/bookings/my")
    public String myBookingsApi(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email == null || email.trim().isEmpty()) {
            return "redirect:/register.html#login";
        }
        return "redirect:/my-bookings.html";
    }
    
    private Double calculateBasePrice(String destination) {
        // Basic pricing logic - you can enhance this
        switch (destination.toLowerCase()) {
            case "mumbai":
            case "india":
                return 460.0;
            case "new york":
            case "usa":
                return 870.0;
            case "rome":
            case "italy":
                return 750.0;
            case "barcelona":
            case "spain":
                return 680.0;
            default:
                return 500.0; // Default price
        }
    }
}
