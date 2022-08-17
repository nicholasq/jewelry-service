package xyz.nicholasq.jewelryservice.infrastructure.api

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import xyz.nicholasq.jewelryservice.infrastructure.data.FirestoreEmulator

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @BeforeAll
    fun init() {
        firestoreEmulator.startFirestoreDb()
    }

    @BeforeEach
    fun initEach() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port.toInt()
        RestAssured.requestSpecification = RequestSpecBuilder()
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build()
        RestAssured.responseSpecification = ResponseSpecBuilder()
            .build()
    }

    @AfterEach
    fun clear() {
        RestAssured.reset()
    }

    @AfterAll
    fun cleanUp() {
        firestoreEmulator.closeFirestoreDb()
    }
}

