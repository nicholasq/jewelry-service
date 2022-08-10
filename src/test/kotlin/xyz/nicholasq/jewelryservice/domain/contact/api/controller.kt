package xyz.nicholasq.jewelryservice.domain.contact.api

import io.restassured.RestAssured
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import xyz.nicholasq.jewelryservice.JewelryServiceApplication
import xyz.nicholasq.jewelryservice.infrastructure.api.AbstractIntegrationTest

@RunWith(SpringRunner::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [JewelryServiceApplication::class]
)
@TestPropertySource(locations = ["classpath:application-test.yml"])
class ContactControllerTest : AbstractIntegrationTest() {

    @Value("\${service.contact.base-path}")
    lateinit var basePath: String

    @Value("\${firestore-image}")
    lateinit var firestore: String

    @Test
    fun createContact() {
        RestAssured.given()
            .`when`()
            .basePath(basePath)
            .body(Contact(firstName = "John", lastName = "Doe"))
            .post()
            .then()
            .statusCode(200)
            .body("firstName", Matchers.equalTo("John"))
    }
}
