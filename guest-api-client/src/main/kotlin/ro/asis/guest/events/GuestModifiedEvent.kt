package ro.asis.guest.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.guest.dto.Guest

data class GuestModifiedEvent(
    @JsonProperty("guestId")
    val guestId: String,

    @JsonProperty("editedGuest")
    val editedGuest: Guest,

    @JsonProperty("eventType")
    val eventType: EventType = EventType.MODIFY
)
