package ro.asis.guest.events

import com.fasterxml.jackson.annotation.JsonProperty
import ro.asis.commons.enums.EventType
import ro.asis.commons.enums.EventType.DELETE

data class GuestDeletedEvent(
    @JsonProperty("guestId")
    val guestId: String,

    @JsonProperty("eventType")
    val eventType: EventType = DELETE
)
