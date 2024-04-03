package com.machado.passin.dto.events;

public record EventDetailDTO(String id,
                             String title,
                             String details,
                             String slug,
                             Integer maximumAttendees,
                             Integer attendeesAmount) {
}
