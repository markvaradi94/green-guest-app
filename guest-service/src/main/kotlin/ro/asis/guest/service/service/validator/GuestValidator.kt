package ro.asis.guest.service.service.validator

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import org.springframework.stereotype.Component
import ro.asis.commons.exceptions.ValidationException
import ro.asis.guest.service.model.entity.GuestEntity
import ro.asis.guest.service.repository.GuestRepository
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of
import java.util.regex.Pattern

@Component
class GuestValidator(
    private val repository: GuestRepository
) {

    fun validateReplaceOrThrow(guestId: String, newGuest: GuestEntity) =
        exists(guestId)
            .or { validate(guest = newGuest, newEntity = false) }
            .ifPresent { throw it }

    fun validateNewOrThrow(guest: GuestEntity) =
        validate(guest = guest, newEntity = true)
            .ifPresent { throw it }

    fun validateExistsOrThrow(guestId: String) = exists(guestId).ifPresent { throw it }

    private fun validate(guest: GuestEntity, newEntity: Boolean): Optional<ValidationException> {
        if (!newEntity) {
            firstNameIsInvalid(guest).ifPresent { throw it }
            lastNameIsInvalid(guest).ifPresent { throw it }
            addressIsInvalid(guest).ifPresent { throw it }
            phoneNumberIsInvalid(guest).ifPresent { throw it }
            emailIsInvalid(guest).ifPresent { throw it }
        }
        return empty()
    }

    private fun lastNameIsInvalid(guest: GuestEntity): Optional<ValidationException> {
        return if (guest.lastName.isBlank()) of(ValidationException("Lastname cannot be blank"))
        else empty()
    }

    private fun firstNameIsInvalid(guest: GuestEntity): Optional<ValidationException> {
        return if (guest.firstName.isBlank()) of(ValidationException("Firstname cannot be blank"))
        else empty()
    }

    private fun addressIsInvalid(guest: GuestEntity): Optional<ValidationException> {
        return when (true) {
            guest.address.city?.isBlank() -> of(ValidationException("City must be valid"))
            guest.address.streetName?.isBlank() -> of(ValidationException("Street name must be valid"))
            guest.address.streetNumber?.isBlank() -> of(ValidationException("Street number must be valid"))
            guest.address.zipCode?.isBlank() -> of(ValidationException("Zipcode must be valid"))
            else -> empty()
        }
    }

    private fun phoneNumberIsInvalid(guest: GuestEntity): Optional<ValidationException> {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val romanianNumberProto: Phonenumber.PhoneNumber

        try {
            romanianNumberProto = phoneUtil.parse(guest.phoneNumber, "RO")
        } catch (exception: NumberParseException) {
            return of(ValidationException(exception.localizedMessage))
        }

        return if (!phoneUtil.isValidNumber(romanianNumberProto)) of(ValidationException("Phone number is not valid"))
        else empty()
    }

    private fun emailIsInvalid(guest: GuestEntity): Optional<ValidationException> {
        val emailIsValid = validateEmailAddress(guest.email)
        return if (!emailIsValid) of(ValidationException("Email address is not valid"))
        else empty()
    }

    private fun validateEmailAddress(email: String): Boolean {
        val validEmailPattern =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
        val matcher = validEmailPattern.matcher(email)
        return matcher.find()
    }

    private fun exists(guestId: String): Optional<ValidationException> {
        return if (repository.existsById(guestId)) empty()
        else of(ValidationException("Guest with id $guestId doesn't exist."))
    }
}
