package ro.asis.guest.service.service

import org.slf4j.LoggerFactory
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ro.asis.guest.events.GuestCreatedEvent
import ro.asis.guest.events.GuestDeletedEvent
import ro.asis.guest.events.GuestModifiedEvent
import ro.asis.guest.service.model.entity.GuestEntity
import ro.asis.guest.service.model.mappers.GuestMapper

@Service
class GuestNotificationsService(
    private val mapper: GuestMapper,
    private val rabbitTemplate: RabbitTemplate,
    private val guestExchange: TopicExchange
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(GuestEntity::class.java)
    }

    fun notifyGuestCreated(guest: GuestEntity) {
        val event = GuestCreatedEvent(guestId = guest.id)

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(guestExchange.name, "green.guests.new", event)
    }

    fun notifyGuestDeleted(guest: GuestEntity) {
        val event = GuestDeletedEvent(guestId = guest.id)

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(guestExchange.name, "green.guests.delete", event)
    }

    fun notifyGuestEdited(guest: GuestEntity) {
        val event = GuestModifiedEvent(
            guestId = guest.id,
            editedGuest = mapper.toApi(guest)
        )

        LOG.info("Sending event $event")
        rabbitTemplate.convertAndSend(guestExchange.name, "green.guests.edit", event)
    }
}
