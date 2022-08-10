package xyz.nicholasq.jewelryservice.infrastructure.api

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import xyz.nicholasq.jewelryservice.infrastructure.data.FirestoreEmulator

abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var firestoreEmulator: FirestoreEmulator

    @LocalServerPort
    lateinit var port: String

    companion object {
        @BeforeAll
        @JvmStatic
        fun loggingInit() {
            RestAssured.filters(RequestLoggingFilter(), ResponseLoggingFilter())
        }
    }

    @BeforeEach
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port.toInt()
        RestAssured.requestSpecification = RequestSpecBuilder()
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build()
        RestAssured.responseSpecification = ResponseSpecBuilder()
            .build()

        firestoreEmulator.startFirestoreDb()
    }

    @AfterEach
    fun clear() {
        RestAssured.reset()
        firestoreEmulator.closeFirestoreDb()
    }
}

