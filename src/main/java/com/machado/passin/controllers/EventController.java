package com.machado.passin.controllers;

import com.machado.passin.dto.attendee.AttendeeListResponseDTO;
import com.machado.passin.dto.events.EventIdDTO;
import com.machado.passin.dto.events.EventRequestDTO;
import com.machado.passin.dto.events.EventResponseDTO;
import com.machado.passin.services.AttendeeService;
import com.machado.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    private final AttendeeService attendeeService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
        EventResponseDTO event = eventService.getEventDetail(id);
        return ResponseEntity
                .ok()
                .body(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO event = eventService.createEvent(body);
        return ResponseEntity
                .created(uriComponentsBuilder.path("/events/{id}").buildAndExpand(event.eventId()).toUri())
                .body(event);
    }

    @GetMapping(path = "/attendees/{id}")
    public ResponseEntity<AttendeeListResponseDTO> getEventAttendee(@PathVariable String id) {
        AttendeeListResponseDTO event = attendeeService.getEventsAttendee(id);
        return ResponseEntity
                .ok()
                .body(event);
    }

}
