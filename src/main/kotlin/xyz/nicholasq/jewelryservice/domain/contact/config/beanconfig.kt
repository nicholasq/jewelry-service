package xyz.nicholasq.jewelryservice.domain.contact.config

import com.google.auth.Credentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.nicholasq.jewelryservice.domain.contact.api.Contact
import xyz.nicholasq.jewelryservice.domain.contact.api.ContactController
import xyz.nicholasq.jewelryservice.domain.contact.data.ContactEntity
import xyz.nicholasq.jewelryservice.domain.contact.data.ContactRepository
import xyz.nicholasq.jewelryservice.domain.contact.data.ContactRepositoryImpl
import xyz.nicholasq.jewelryservice.domain.contact.mapper.ContactToEntityMapper
import xyz.nicholasq.jewelryservice.domain.contact.mapper.EntityToContactMapper
import xyz.nicholasq.jewelryservice.domain.contact.service.ContactService
import xyz.nicholasq.jewelryservice.infrastructure.api.BaseCrudController
import xyz.nicholasq.jewelryservice.infrastructure.api.CrudController
import xyz.nicholasq.jewelryservice.infrastructure.config.CrudConfig
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncCrudRepository
import xyz.nicholasq.jewelryservice.infrastructure.data.DataSource
import xyz.nicholasq.jewelryservice.infrastructure.data.FirestoreDataSource
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DefaultDomainObjectMapper
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DomainObjectMapper
import xyz.nicholasq.jewelryservice.infrastructure.service.BaseCrudService
import xyz.nicholasq.jewelryservice.infrastructure.service.CrudService

@Configuration
class ContactCrudConfig : CrudConfig<Contact, ContactEntity, String> {

    @Value("\${contact.firestore.collection}")
    lateinit var contactCollection: String

    @Autowired
    lateinit var dataSource: DataSource<Firestore>

    @Bean("defaultContactController")
    override fun crudController(): CrudController<Contact, String> {
        return BaseCrudController(crudService(), LoggerFactory.getLogger(ContactController::class.java))
    }

    @Bean("contactCrudService")
    override fun crudService(): CrudService<Contact, String> {
        return BaseCrudService(
            domainObjectMapper(),
            crudRepository(),
            LoggerFactory.getLogger(ContactService::class.java)
        )
    }

    @Bean("contactCrudRepository")
    override fun crudRepository(): AsyncCrudRepository<ContactEntity, String> {
        return ContactRepositoryImpl(
            dataSource.get(),
            contactCollection,
            LoggerFactory.getLogger(ContactRepository::class.java)
        )
    }

    @Bean("contactDomainObjectMapper")
    override fun domainObjectMapper(): DomainObjectMapper<Contact, ContactEntity> {
        return DefaultDomainObjectMapper(ContactToEntityMapper(), EntityToContactMapper())
    }
}

@Configuration
class DataSourceConfig {

    @Value("\${gcloud.projectId}")
    lateinit var gcpProjectId: String

    @Autowired
    lateinit var googleCredentials: Credentials

    @Bean
    fun dataSource(): DataSource<Firestore> {
        val firestoreOptions = buildFirestoreOptions(gcpProjectId, googleCredentials)
        return FirestoreDataSource(firestoreOptions.service)
    }

}

@Configuration
class GcpConfig {

    @Bean
    fun googleCredentials(): Credentials {
        return GoogleCredentials.getApplicationDefault()
    }

}

fun buildFirestoreOptions(projectId: String, credentials: Credentials): FirestoreOptions {

    return FirestoreOptions.getDefaultInstance().toBuilder()
        .setProjectId(projectId)
        .setCredentials(credentials)
        .build()
}

