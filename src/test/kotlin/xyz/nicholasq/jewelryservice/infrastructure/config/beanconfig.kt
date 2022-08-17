package xyz.nicholasq.jewelryservice.infrastructure.config

import com.google.auth.Credentials
import com.google.cloud.NoCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import xyz.nicholasq.jewelryservice.infrastructure.data.DataSource
import xyz.nicholasq.jewelryservice.infrastructure.data.FirestoreDataSource
import xyz.nicholasq.jewelryservice.infrastructure.data.FirestoreEmulator

@Configuration
class LocalIntegrationTestConfig {

    @Value("\${gcloud.projectId}")
    lateinit var gcpProjectId: String

    @Value("\${contact.firestore.collection}")
    lateinit var contactCollection: String

    @Value("\${firestore-image}")
    lateinit var firestoreImage: String

    @Autowired
    @Qualifier("testGoogleCredentials")
    lateinit var googleCredentials: Credentials

    @Bean("testFirestoreEmulator")
    @Primary
    fun firestoreEmulator(): FirestoreEmulator {
        return FirestoreEmulator(firestoreImage)
    }

    @Primary
    @Bean("testDataSource")
    fun dataSource(): DataSource<Firestore> {
        return FirestoreDataSource(
            FirestoreOptions
                .getDefaultInstance()
                .toBuilder()
                .setHost(firestoreEmulator().emulatorEndpoint)
                .setCredentials(googleCredentials)
                .setProjectId(gcpProjectId)
                .build().service
        )
    }

}

@Configuration
class LocalIntegrationTestGcpConfig {

    @Primary
    @Bean("testGoogleCredentials")
    fun googleCredentials(): Credentials {
        return NoCredentials.getInstance()
    }

}
