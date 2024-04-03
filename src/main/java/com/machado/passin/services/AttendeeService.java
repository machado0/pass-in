package com.machado.passin.services;

import com.machado.passin.domain.attendee.Attendee;
import com.machado.passin.domain.attendee.exceptions.AttendeeAlreadyExistsException;
import com.machado.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.machado.passin.domain.checkin.CheckIn;
import com.machado.passin.dto.attendee.AttendeeBadgeDTO;
import com.machado.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.machado.passin.dto.attendee.AttendeeDetailsDTO;
import com.machado.passin.dto.attendee.AttendeeListResponseDTO;
import com.machado.passin.repositories.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return attendeeRepository.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailsDTO> attendeeDetailsList = attendeeList.stream().map(attendee -> {
                    Optional<CheckIn> checkIn = checkInService.getCheckIn(attendee.getId());
                    LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
                    return new AttendeeDetailsDTO(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
                })
                .toList();
        return new AttendeeListResponseDTO(attendeeDetailsList);
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        return attendeeRepository.save(newAttendee);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        attendeeRepository.findByEventIdAndEmail(eventId, email)
                .ifPresent(attendee -> new AttendeeAlreadyExistsException("Attendee " + attendee.getEmail() + " is already registered"));
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/events/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri();

        return new AttendeeBadgeResponseDTO(new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId()));
    }

    public CheckIn checkInAttendee(String attendeeId) {
        return checkInService.checkInAttendee(getAttendee(attendeeId));
    }

    private Attendee getAttendee(String attendeeId) {
        return attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID:" + attendeeId));
    }
}
