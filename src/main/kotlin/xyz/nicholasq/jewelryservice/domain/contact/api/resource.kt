package xyz.nicholasq.jewelryservice.domain.contact.api

import xyz.nicholasq.jewelryservice.infrastructure.api.Dto
import java.time.ZonedDateTime
import javax.annotation.Nonnull

data class Contact(
    override var id: String? = null,
    @field:Nonnull
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: ZonedDateTime? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val company: String? = null,
    val jobTitle: String? = null,
    val notes: String? = null
) : Dto(id)
