package xyz.nicholasq.jewelryservice.domain.contact.api

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import xyz.nicholasq.jewelryservice.infrastructure.api.CrudController

@RestController
@RequestMapping("\${contact.api.base-path}")
class ContactController(
    @param:Qualifier("defaultContactController") private val crudController: CrudController<Contact, String>
) : CrudController<Contact, String> {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override suspend fun create(@Validated @RequestBody resource: Contact): Contact {
        return crudController.create(resource)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    override suspend fun find(@PathVariable("id") id: String): Contact {
        return crudController.find(id)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    override suspend fun update(@PathVariable("id") id: String, @RequestBody resource: Contact): Contact {
        return crudController.update(id, resource)
    }

    @DeleteMapping("/{id}")
    override suspend fun delete(@PathVariable("id") id: String): ResponseEntity<Unit> {
        return crudController.delete(id)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    override suspend fun findAll(): List<Contact> {
        return crudController.findAll()
    }
}
