package com.example.travel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.travel.model.Booking;
import com.example.travel.repository.BookingRepository;

@Controller
public class WebController {

    private final BookingRepository bookingRepository;

    public WebController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/booking-confirmation.html")
    public String bookingConfirmation(@RequestParam(required = false) String bookingId, Model model) {
        if (bookingId != null && !bookingId.isEmpty()) {
            try {
                Long id = Long.parseLong(bookingId);
                Booking booking = bookingRepository.findById(id).orElse(null);
                
                if (booking != null) {
                    model.addAttribute("booking", booking);
                    model.addAttribute("bookingId", booking.getBookingId());
                    model.addAttribute("destination", booking.getDestination());
                    model.addAttribute("travelDate", booking.getTravelDate());
                    model.addAttribute("guests", booking.getGuests());
                    model.addAttribute("totalPrice", booking.getTotalPrice());
                    model.addAttribute("specialRequests", booking.getSpecialRequests());
                }
            } catch (NumberFormatException e) {
                // Invalid booking ID, just show confirmation page without details
            }
        }
        return "booking-confirmation";
    }
}
