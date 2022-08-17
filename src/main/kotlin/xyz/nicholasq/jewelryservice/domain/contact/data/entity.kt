package xyz.nicholasq.jewelryservice.domain.contact.data

import xyz.nicholasq.jewelryservice.infrastructure.data.Entity

data class ContactEntity(
    override var id: String?,
    val firstName: String?,
    val lastName: String?,
    val dateOfBirth: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val company: String?,
    val jobTitle: String?,
    val notes: String?
) : Entity(id)
