package xyz.nicholasq.jewelryservice.infrastructure.data

import org.testcontainers.containers.FirestoreEmulatorContainer
import org.testcontainers.utility.DockerImageName

class FirestoreEmulator(
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
