package com.medical.appointment.controller;

import com.medical.appointment.dto.request.JoinWaitlistRequest;
import com.medical.appointment.dto.response.WaitlistResponse;
import com.medical.appointment.service.WaitlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waitlist")
public class WaitlistController {

    private final WaitlistService waitlistService;

    public WaitlistController(WaitlistService waitlistService) {
        this.waitlistService = waitlistService;
    }


    @PostMapping
    public ResponseEntity<WaitlistResponse> joinWaitlist(
            @Valid @RequestBody JoinWaitlistRequest request) {
        return new ResponseEntity<>(
                waitlistService.joinWaitlist(request),
                HttpStatus.CREATED);
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<WaitlistResponse>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(waitlistService.getWaitlistByPatient(patientId));
    }


    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<WaitlistResponse>> getByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(waitlistService.getWaitlistByDoctor(doctorId));
    }


    @GetMapping("/{waitlistId}/position")
    public ResponseEntity<Integer> getQueuePosition(
            @PathVariable Long waitlistId) {
        return ResponseEntity.ok(waitlistService.getQueuePosition(waitlistId));
    }


    @DeleteMapping("/{waitlistId}/cancel")
    public ResponseEntity<Void> cancelWaitlistEntry(
            @PathVariable Long waitlistId,
            @RequestParam Long patientId) {
        waitlistService.cancelWaitlistEntry(waitlistId, patientId);
        return ResponseEntity.noContent().build();
    }
}
