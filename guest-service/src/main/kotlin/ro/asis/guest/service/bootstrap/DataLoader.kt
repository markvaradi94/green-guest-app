package ro.asis.guest.service.bootstrap

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ro.asis.commons.model.Address
import ro.asis.commons.model.Cart
import ro.asis.guest.service.model.entity.GuestEntity
import ro.asis.guest.service.service.GuestService

@Component
class DataLoader(private val service: GuestService) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        service.addGuest(
//            GuestEntity(
//                firstName = "Sanyi",
//                lastName = "Fekete",
//                phoneNumber = "0721 113 225",
//                email = "sanyi@fekete.hu",
//                address = Address(),
//                cart = Cart()
//            )
//        )
    }
}
