package xyz.nicholasq.jewelryservice.domain.contact.api

import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import xyz.nicholasq.jewelryservice.JewelryServiceApplication
import xyz.nicholasq.jewelryservice.infrastructure.api.AbstractIntegrationTest
import xyz.nicholasq.jewelryservice.infrastructure.config.LocalIntegrationTestConfig
import xyz.nicholasq.jewelryservice.infrastructure.config.LocalIntegrationTestGcpConfig
import java.time.ZonedDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [JewelryServiceApplication::class, LocalIntegrationTestConfig::class, LocalIntegrationTestGcpConfig::class]
)
@TestPropertySource(locations = ["classpath:application-test.yml"])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContactControllerTest : AbstractIntegrationTest() {

    @Value("\${contact.api.base-path}")
    lateinit var basePath: String

    var contactId: String? = null

    @Order(1)
    @Test
    fun createContact() {

        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .body(Contact(firstName = "John", lastName = "Doe", dateOfBirth = ZonedDateTime.now()))
            .post()
            .then()
            .statusCode(200)
            .body("firstName", Matchers.equalTo("John"))
            .extract()

        contactId = response.`as`(Contact::class.java).id

        // Create a couple more, so we can test findAll() later on
        RestAssured.given()
            .`when`()
            .basePath(basePath)
            .body(Contact(firstName = "Mickey", lastName = "Mouse", dateOfBirth = ZonedDateTime.now()))
            .post()
            .then()
            .statusCode(200)
            .body("firstName", Matchers.equalTo("Mickey"))
            .extract()

        RestAssured.given()
            .`when`()
            .basePath(basePath)
            .body(Contact(firstName = "Donald", lastName = "Duck", dateOfBirth = ZonedDateTime.now()))
            .post()
            .then()
            .statusCode(200)
            .body("firstName", Matchers.equalTo("Donald"))
            .extract()
    }

    @Order(1)
    @Test
    fun findContact() {

        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .pathParam("id", contactId)
            .get("/{id}")
            .then()
            .statusCode(200)
            .body("firstName", Matchers.equalTo("John"))
            .body("lastName", Matchers.equalTo("Doe"))
            .extract()
    }

    @Order(2)
    @Test
    fun updateContact() {

        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .body(Contact(firstName = "Johnny", lastName = "Doe", dateOfBirth = ZonedDateTime.now()))
            .pathParam("id", contactId)
            .put("/{id}")
            .then()
            .statusCode(200)
            .body("firstName", Matchers.equalTo("Johnny"))
            .body("lastName", Matchers.equalTo("Doe"))
            .extract()
    }

    @Order(3)
    @Test
    fun findAll() {
        val response = RestAssured.given()
            .`when`()
            .basePath(basePath)
            .get()
            .then()
            .statusCode(200)
//            .body("size()", Matchers.equalTo(3))
            .extract()
        response.`as`(Array<Contact>::class.java).forEach {
            println(it)
        }
    }

    @Order(4)
    @Test
    fun delete() {
        RestAssured.given()
            .`when`()
            .basePath(basePath)
            .pathParam("id", contactId)
            .delete("/{id}")
            .then()
            .statusCode(201)

        RestAssured.given()
            .`when`()
            .basePath(basePath)
            .pathParam("id", contactId)
            .get("/{id}")
            .then()
            .statusCode(404)
    }
}
