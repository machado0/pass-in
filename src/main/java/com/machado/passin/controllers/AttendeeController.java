package com.machado.passin.controllers;

import com.machado.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.machado.passin.services.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;

    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity
                .ok(attendeeService.getAttendeeBadge(attendeeId, uriComponentsBuilder));
    }

    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity<AttendeeBadgeResponseDTO> registerCheckIn(@PathVariable String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        return ResponseEntity
                .created(uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeId).toUri())
                .build();
    }
}
