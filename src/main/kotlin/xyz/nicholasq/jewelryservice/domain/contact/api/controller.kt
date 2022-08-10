package xyz.nicholasq.jewelryservice.domain.contact.api

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import xyz.nicholasq.jewelryservice.infrastructure.api.CrudController

@RestController("contactController")
@RequestMapping("\${service.contact.base-path}")
class ContactController(@param:Qualifier("baseCrudController") private val crudController: CrudController<Contact>) :
    CrudController<Contact> {

    @PostMapping
    override suspend fun create(@RequestBody createCommand: Contact): Contact {
        return crudController.create(createCommand)
    }

    @GetMapping("/{id}")
    override suspend fun find(@PathVariable("id") id: String): Contact {
        return crudController.find(id)
    }

    @PutMapping("/{id}")
    override suspend fun update(@PathVariable("id") id: String, @RequestBody updateCommand: Contact): Contact {
        return crudController.update(id, updateCommand)
    }

    @DeleteMapping("/{id}")
    override suspend fun delete(@PathVariable("id") id: String): HttpStatus {
        return crudController.delete(id)
    }

    @GetMapping
    override suspend fun findAll(): List<Contact> {
        return crudController.findAll()
    }
}
