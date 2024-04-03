package com.machado.passin.services;

import com.machado.passin.domain.attendee.Attendee;
import com.machado.passin.domain.event.Event;
import com.machado.passin.domain.event.exceptions.EventFullException;
import com.machado.passin.domain.event.exceptions.EventNotFoundException;
import com.machado.passin.dto.attendee.AttendeeIdDTO;
import com.machado.passin.dto.attendee.AttendeeRequestDTO;
import com.machado.passin.dto.events.EventIdDTO;
import com.machado.passin.dto.events.EventRequestDTO;
import com.machado.passin.dto.events.EventResponseDTO;
import com.machado.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID:" + eventId));
        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = Event.builder()
                .title(eventDTO.title())
                .detail(eventDTO.details())
                .maximumAttendees(eventDTO.maximumAttendees())
                .slug(createSlug(eventDTO.title()))
                .build();

        newEvent = eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(AttendeeRequestDTO attendeeRequestDTO, String eventId) {
        attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);

        EventResponseDTO event = getEventDetail(eventId);

        if (event.getEvent().maximumAttendees() <= event.getEvent().attendeesAmount()) {
            throw new EventFullException("Event is full");
        }

        Attendee newAttendee = Attendee.builder()
                .name(attendeeRequestDTO.name())
                .email(attendeeRequestDTO.email())
                .event(Event.builder()
                        .id(eventId)
                        .build())
                .createdAt(LocalDateTime.now())
                .build();


        return new AttendeeIdDTO(attendeeService.registerAttendee(newAttendee).getId());
    }

    private String createSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
