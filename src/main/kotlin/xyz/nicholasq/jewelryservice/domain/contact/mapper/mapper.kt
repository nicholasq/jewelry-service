package xyz.nicholasq.jewelryservice.domain.contact.mapper

import xyz.nicholasq.jewelryservice.domain.contact.api.Contact
import xyz.nicholasq.jewelryservice.domain.contact.data.ContactEntity
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DtoToEntityObjectMapper
import xyz.nicholasq.jewelryservice.infrastructure.mapper.EntityToDtoObjectMapper
import java.time.ZonedDateTime

class ContactToEntityMapper : DtoToEntityObjectMapper<Contact, ContactEntity> {
    override fun map(obj: Contact): ContactEntity {
        return ContactEntity(
            id = obj.id,
            firstName = obj.firstName,
            lastName = obj.lastName,
            dateOfBirth = obj.dateOfBirth.toString(),
            email = obj.email,
            phone = obj.phone,
            address = obj.address,
            company = obj.company,
            jobTitle = obj.jobTitle,
            notes = obj.notes
        )
    }
}

class EntityToContactMapper : EntityToDtoObjectMapper<ContactEntity, Contact> {
    override fun map(obj: ContactEntity): Contact {
        return Contact(
            id = obj.id,
            firstName = obj.firstName,
            lastName = obj.lastName,
            dateOfBirth = ZonedDateTime.parse(obj.dateOfBirth),
            email = obj.email,
            phone = obj.phone,
            address = obj.address,
            company = obj.company,
            jobTitle = obj.jobTitle,
            notes = obj.notes
        )
    }
}
