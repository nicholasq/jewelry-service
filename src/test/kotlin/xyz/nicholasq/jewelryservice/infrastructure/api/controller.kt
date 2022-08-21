package xyz.nicholasq.jewelryservice.infrastructure.api

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import xyz.nicholasq.jewelryservice.JewelryServiceApplication
import xyz.nicholasq.jewelryservice.infrastructure.config.LocalIntegrationTestConfig
import xyz.nicholasq.jewelryservice.infrastructure.config.LocalIntegrationTestGcpConfig
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

@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [JewelryServiceApplication::class, LocalIntegrationTestConfig::class, LocalIntegrationTestGcpConfig::class]
)
@TestPropertySource(locations = ["classpath:application-test.yml"])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractCrudControllerTest<T : Resource> : AbstractIntegrationTest() {

    var resourceId: String? = null

    abstract var basePath: String

    abstract fun buildFirstResource(): T

    abstract fun buildUpdateResource(): T

    abstract fun verifyFirstResource(response: ValidatableResponse)

    abstract fun verifyFindResource(response: ValidatableResponse)

    abstract fun verifyUpdateResource(response: ValidatableResponse)

    abstract fun verifyFindAllResources(response: ValidatableResponse)


    @Order(1)
    @Test
    fun createResource() {

        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .body(buildFirstResource())
            .post()
            .then()

        verifyFirstResource(response)

        resourceId = response.extract().body().jsonPath().getString("id")

    }

    @Order(2)
    @Test
    fun findResource() {

        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .pathParam("id", resourceId)
            .get("/{id}")
            .then()

        verifyFindResource(response)
    }

    @Order(3)
    @Test
    fun updateResource() {

        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .body(buildUpdateResource())
            .pathParam("id", resourceId)
            .put("/{id}")
            .then()

        verifyUpdateResource(response)
    }

    @Order(4)
    @Test
    fun findAllResources() {
        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .get()
            .then()
        verifyFindAllResources(response)
    }

    @Order(5)
    @Test
    fun delete() {
        RestAssured.given()
            .`when`()
            .basePath(basePath)
            .pathParam("id", resourceId)
            .delete("/{id}")
            .then()
            .statusCode(204)

        RestAssured.given()
            .`when`()
            .basePath(basePath)
            .pathParam("id", resourceId)
            .get("/{id}")
            .then()
            .statusCode(404)
    }
}
