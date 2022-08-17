package xyz.nicholasq.jewelryservice.infrastructure.service

import org.slf4j.Logger
import xyz.nicholasq.jewelryservice.infrastructure.api.Dto
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncCrudRepository
import xyz.nicholasq.jewelryservice.infrastructure.data.Entity
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DomainObjectMapper

interface CrudService<T, ID> {
    suspend fun create(dto: T): T
    suspend fun update(id: ID, dto: T): T
    suspend fun findById(id: ID): T
    suspend fun findAll(): List<T>
    suspend fun delete(id: ID): Boolean
}

class BaseCrudService<T1 : Dto, T2 : Entity, ID>(
    private val domainObjectMapper: DomainObjectMapper<T1, T2>,
    private val repository: AsyncCrudRepository<T2, ID>,
    private val logger: Logger
) : CrudService<T1, ID> {

    var postSaveProcess = { dto: T1 -> dto }

    var postUpdateProcess = { dto: T1 -> dto }

    var postDeleteProcess = { dto: T1 -> dto }

    override suspend fun create(dto: T1): T1 {
        logger.debug("save() - dto: $dto")
        val entity: T2 = domainObjectMapper.toEntity(dto)
        val savedEntity: T2 = repository.save(entity)
        val savedDto: T1 = domainObjectMapper.toDto(savedEntity)
        postSaveProcess(savedDto)
        logger.debug("save() - savedDto: $savedDto")
        return savedDto
    }

    override suspend fun update(id: ID, dto: T1): T1 {
        logger.debug("update() - dto: $dto")
        val entity: T2 = domainObjectMapper.toEntity(dto)
        val updatedEntity: T2 = repository.save(entity)
        val updatedDto: T1 = domainObjectMapper.toDto(updatedEntity)
        postUpdateProcess(updatedDto)
        logger.debug("update() - updatedDto: $updatedDto")
        return updatedDto
    }

    override suspend fun findById(id: ID): T1 {
        logger.debug("findById() - id: $id")
        val entity: T2 = repository.findById(id)
        val dto: T1 = domainObjectMapper.toDto(entity)
        logger.debug("findById() - dto: $dto")
        return dto
    }

    override suspend fun findAll(): List<T1> {
        logger.debug("findAll()")
        val entities: List<T2> = repository.findAll()
        val dtoList: List<T1> = entities.map { domainObjectMapper.toDto(it) }
        logger.debug("findAll() - dtoList: $dtoList")
        return dtoList
    }

    override suspend fun delete(id: ID): Boolean {
        logger.debug("delete() - id: $id")
        val dto = findById(id)
        val deleted = repository.delete(id)
        logger.debug("delete() - deleted: $deleted dto: $dto")
        postDeleteProcess(dto)
        return deleted
    }
}

