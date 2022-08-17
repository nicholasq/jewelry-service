package xyz.nicholasq.jewelryservice.domain.contact.data

import com.google.cloud.firestore.Firestore
import org.slf4j.Logger
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncCrudRepository
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncFirestoreCrudRepository

interface ContactRepository<T, ID> : AsyncCrudRepository<T, ID>

class ContactRepositoryImpl(firestore: Firestore, collection: String, logger: Logger) :
    AsyncFirestoreCrudRepository<ContactEntity, String>(firestore, collection, logger) {
    override fun entityClass(): Class<ContactEntity> {
        return ContactEntity::class.java
    }
}
