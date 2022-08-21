package xyz.nicholasq.jewelryservice.domain.contact.api

import io.restassured.response.ValidatableResponse
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Value
import xyz.nicholasq.jewelryservice.infrastructure.api.AbstractCrudControllerTest
import java.time.ZonedDateTime

class ContactControllerTest : AbstractCrudControllerTest<Contact>() {

    @Value("\${contact.api.base-path}")
    override lateinit var basePath: String

    override fun buildFirstResource(): Contact {
        return Contact(
            firstName = "John",
            lastName = "Doe",
            dateOfBirth = ZonedDateTime.now().toString()
        )
    }

    override fun buildUpdateResource(): Contact {
        return Contact(
            firstName = "Johnny",
            lastName = "Doe",
            dateOfBirth = ZonedDateTime.now().toString()
        )
    }

    override fun verifyFirstResource(response: ValidatableResponse) {
        response
            .statusCode(201)
            .body("firstName", Matchers.equalTo("John"))
            .body("lastName", Matchers.equalTo("Doe"))
    }

    override fun verifyFindResource(response: ValidatableResponse) {
        response.statusCode(200)
            .body("firstName", Matchers.equalTo("John"))
            .body("lastName", Matchers.equalTo("Doe"))
    }

    override fun verifyUpdateResource(response: ValidatableResponse) {
        response.statusCode(200)
            .body("firstName", Matchers.equalTo("Johnny"))
            .body("lastName", Matchers.equalTo("Doe"))
    }

    override fun verifyFindAllResources(response: ValidatableResponse) {
        println("todo: implement")
    }

}
