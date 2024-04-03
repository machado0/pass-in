package com.machado.passin.services;

import com.machado.passin.domain.attendee.Attendee;
import com.machado.passin.domain.checkin.CheckIn;
import com.machado.passin.dto.attendee.AttendeeDetails;
import com.machado.passin.dto.attendee.AttendeeListResponseDTO;
import com.machado.passin.repositories.AttendeeRepository;
import com.machado.passin.repositories.CheckInRepository;
import com.machado.passin.repositories.EventRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return attendeeRepository.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = checkInRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        })
                .toList();
        return new AttendeeListResponseDTO(attendeeDetailsList);
    }
}
