package com.machado.passin.services;

import com.machado.passin.domain.attendee.Attendee;
import com.machado.passin.domain.checkin.CheckIn;
import com.machado.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.machado.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;

    public CheckIn checkInAttendee(Attendee attendee) {
        verifyCheckInExists(attendee.getId());

        CheckIn checkIn = CheckIn.builder()
                .attendee(attendee)
                .createdAt(LocalDateTime.now())
                .build();

        return checkInRepository.save(checkIn);
    }

    public Optional<CheckIn> getCheckIn(String attendeeId) {
        return checkInRepository.findByAttendeeId(attendeeId);
    }

    private void verifyCheckInExists(String attendeeId) {
        if (getCheckIn(attendeeId).isPresent()) {
            throw new CheckInAlreadyExistsException("Attendee already checked in");
        }
    }


}
