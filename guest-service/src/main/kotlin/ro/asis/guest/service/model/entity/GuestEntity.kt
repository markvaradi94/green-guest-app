package ro.asis.guest.service.model.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ro.asis.commons.model.Address
import ro.asis.commons.model.Cart

@Document(collection = "guests")
class GuestEntity(
    @Id
    var id: String = ObjectId.get().toHexString(),

    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var email: String,
    var address: Address,
    var cart: Cart
)
