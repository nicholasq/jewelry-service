package xyz.nicholasq.jewelryservice.infrastructure.api

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

interface CrudController<T : Dto> {
    suspend fun create(createCommand: T): T

    suspend fun find(id: String): T

    suspend fun update(id: String, updateCommand: T): T

    suspend fun delete(id: String): HttpStatus

    suspend fun findAll(): List<T>
}

@Component("baseCrudController")
class BaseCrudController<T : Dto> : CrudController<T> {
    override suspend fun create(createCommand: T): T {
        return createCommand
    }

    override suspend fun find(id: String): T {
        TODO("not implemented")
    }

    override suspend fun update(id: String, updateCommand: T): T {
        TODO("not implemented")
    }

    override suspend fun delete(id: String): HttpStatus {
        TODO("not implemented")
    }

    override suspend fun findAll(): List<T> {
        TODO("not implemented")
    }
}
