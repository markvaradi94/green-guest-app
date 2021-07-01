package ro.asis.guest.service.model.mappers

import org.springframework.stereotype.Component
import ro.asis.commons.utils.ModelMapper
import ro.asis.guest.dto.Guest
import ro.asis.guest.service.model.entity.GuestEntity

@Component
class GuestMapper : ModelMapper<Guest, GuestEntity> {
    override fun toApi(source: GuestEntity): Guest {
        return Guest(
            id = source.id,
            firstName = source.firstName,
            lastName = source.lastName,
            phoneNumber = source.phoneNumber,
            email = source.email,
            address = source.address,
            cart = source.cart
        )
    }

    override fun toEntity(source: Guest): GuestEntity {
        return GuestEntity(
            id = source.id,
            firstName = source.firstName,
            lastName = source.lastName,
            phoneNumber = source.phoneNumber,
            email = source.email,
            address = source.address,
            cart = source.cart
        )
    }
}
