package com.machado.passin.dto.events;

import com.machado.passin.domain.event.Event;
import lombok.Getter;

@Getter
public class EventResponseDTO {

    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees) {
        this.event = new EventDetailDTO(event.getId(), event.getTitle(), event.getDetail(), event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);

    }
}
