package com.spring.vaidya.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.Slot;
import com.spring.vaidya.entity.SlotRequest;
import com.spring.vaidya.exception.SlotNotFoundException;
import com.spring.vaidya.service.SlotService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller for managing slot-related operations.
 */
@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "http://localhost:5173") // Allows frontend requests from this origin
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Slot Booking API", description = "APIs for booking and managing patient slots"))
public class SlotController {

    private static final Logger logger = LoggerFactory.getLogger(SlotController.class);

    @Autowired
    private SlotService slotService;

    @Operation(summary = "Create slots", description = "Creates slots based on given time range, slot duration, user ID, and date.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Slots created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/create")
    public ResponseEntity<List<Slot>> createSlots(@RequestBody SlotRequest slotRequest) {
        logger.info("Received request to create slots: {}", slotRequest);

        LocalTime startTime = slotRequest.getStartTime();
        LocalTime endTime = slotRequest.getEndTime();
        String slotRange = slotRequest.getSlotRange();
        Long userId = slotRequest.getUserId();
        LocalDate date = slotRequest.getDate();

        List<Slot> slots = slotService.createSlots(startTime, endTime, slotRange, userId, date);
        logger.info("Created {} slots successfully", slots.size());

        return ResponseEntity.ok(slots);
    }

    @Operation(summary = "Update slot status", description = "Updates the status of a slot (e.g., available, booked).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Slot updated successfully"),
        @ApiResponse(responseCode = "404", description = "Slot not found")
    })
    @PutMapping("/{slotId}")
    public ResponseEntity<?> updateSlotStatus(@PathVariable Long slotId, @RequestParam String status) {
        logger.info("Updating slot {} with status {}", slotId, status);
        try {
            Slot slot = slotService.updateSlotStatus(slotId, status);
            logger.info("Slot {} updated successfully to {}", slotId, status);
            return ResponseEntity.ok(slot);
        } catch (SlotNotFoundException ex) {
            logger.error("Slot {} not found", slotId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Operation(summary = "Check slot availability", description = "Checks if a specific slot is available.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Availability status returned successfully"),
        @ApiResponse(responseCode = "404", description = "Slot not found")
    })
    @GetMapping("/{slotId}/availability")
    public ResponseEntity<Boolean> isSlotAvailable(@PathVariable Long slotId) {
        logger.info("Checking availability for slot {}", slotId);
        boolean available = slotService.isSlotAvailable(slotId);
        logger.info("Slot {} availability: {}", slotId, available);
        return ResponseEntity.ok(available);
    }

    @Operation(summary = "Get slots by date", description = "Retrieves all slots available on a specific date.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Slots retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No slots found for the given date")
    })
    @GetMapping("/by-date")
    public ResponseEntity<?> getSlotsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("Fetching slots for date: {}", date);
        List<Slot> slots = slotService.getSlotsByDate(date);
        if (slots.isEmpty()) {
            logger.warn("No slots found for date {}", date);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No slots found for the given date.");
        }
        logger.info("Found {} slots for date {}", slots.size(), date);
        return ResponseEntity.ok(slots);
    }

    @Operation(summary = "Get slots by date and user ID", description = "Retrieves slots for a specific user (doctor) on a given date.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Slots retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No slots found for the given date and user")
    })
    @GetMapping("/search")
    public ResponseEntity<?> getSlotsByDateAndUserId(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("userId") Long userId) {
        logger.info("Fetching slots for date {} and userId {}", date, userId);
        List<Slot> slots = slotService.getSlotsByDateAndUserId(date, userId);
        if (slots.isEmpty()) {
            logger.warn("No slots found for date {} and userId {}", date, userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No slots found for the given date and user.");
        }
        logger.info("Found {} slots for date {} and userId {}", slots.size(), date, userId);
        return ResponseEntity.ok(slots);
    }
}
