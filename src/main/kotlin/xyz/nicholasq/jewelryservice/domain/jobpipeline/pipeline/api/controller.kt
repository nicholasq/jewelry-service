package xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.api

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.nicholasq.jewelryservice.infrastructure.api.CrudController

@RestController
@RequestMapping("\${pipeline.api.base-path}")
class PipelineController(
    @param:Qualifier("defaultPipelineController") private val crudController: CrudController<Pipeline, String>
) : CrudController<Pipeline, String> {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override suspend fun create(resource: Pipeline): Pipeline {
        return crudController.create(resource)
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    override suspend fun find(id: String): Pipeline {
        return crudController.find(id)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    override suspend fun update(id: String, resource: Pipeline): Pipeline {
        return crudController.update(id, resource)
    }

    @DeleteMapping("/{id}")
    override suspend fun delete(id: String): ResponseEntity<Unit> {
        return crudController.delete(id)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    override suspend fun findAll(): List<Pipeline> {
        return crudController.findAll()
    }
}
