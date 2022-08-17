package xyz.nicholasq.jewelryservice.infrastructure.api

import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import xyz.nicholasq.jewelryservice.infrastructure.data.DocumentNotFoundException
import xyz.nicholasq.jewelryservice.infrastructure.service.CrudService

interface CrudController<T, ID> {
    suspend fun create(resource: T): T

    suspend fun find(id: ID): T

    suspend fun update(id: ID, resource: T): T

    suspend fun delete(id: ID): HttpStatus

    suspend fun findAll(): List<T>
}

class BaseCrudController<T : Dto, ID>(
    private val crudService: CrudService<T, ID>,
    private val logger: Logger
) : CrudController<T, ID> {

    override suspend fun create(resource: T): T {
        logger.debug("create() - resource: $resource")
        return crudService.create(resource)
    }

    override suspend fun find(id: ID): T {
        logger.debug("find() - id: $id")
        try {
            return crudService.findById(id)
        } catch (e: DocumentNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "document '$id' not found", e)
        }
    }

    override suspend fun update(id: ID, resource: T): T {
        logger.debug("update() - id: $id, resource: $resource")
        try {
            return crudService.update(id, resource)
        } catch (e: DocumentNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "document '$id' not found", e)
        }
    }

    override suspend fun delete(id: ID): HttpStatus {
        logger.debug("delete() - id: $id")
        return if (crudService.delete(id)) HttpStatus.NO_CONTENT else HttpStatus.NOT_FOUND
    }

    override suspend fun findAll(): List<T> {
        logger.debug("findAll()")
        return crudService.findAll()
    }

}
