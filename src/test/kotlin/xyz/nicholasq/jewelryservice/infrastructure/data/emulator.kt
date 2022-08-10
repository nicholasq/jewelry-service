package xyz.nicholasq.jewelryservice.infrastructure.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.testcontainers.containers.FirestoreEmulatorContainer
import org.testcontainers.utility.DockerImageName

@Component
class FirestoreEmulator(
    @param:Value("\${firestore-image}")
    private val image: String
) {

    private var firestoreDBContainer: FirestoreEmulatorContainer? = null

    val emulatorEndpoint: String
        get() {
            if (firestoreDBContainer == null || !firestoreDBContainer!!.isRunning) {
                startFirestoreDb()
            }
            return firestoreDBContainer!!.emulatorEndpoint
        }

    fun startFirestoreDb() {
        if (firestoreDBContainer == null) {
            firestoreDBContainer = FirestoreEmulatorContainer(DockerImageName.parse(image))
                .withExposedPorts(8080)
        }
        if (!firestoreDBContainer!!.isRunning) {
            firestoreDBContainer!!.start()
        }
    }

    fun closeFirestoreDb() {
        if (firestoreDBContainer != null) {
            firestoreDBContainer!!.close()
        }
    }
}
