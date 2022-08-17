package xyz.nicholasq.jewelryservice.infrastructure.mapper

import xyz.nicholasq.jewelryservice.infrastructure.api.Dto
import xyz.nicholasq.jewelryservice.infrastructure.data.Entity

interface ObjectMapper<T1, T2> {
    fun map(obj: T1): T2
}

interface EntityToDtoObjectMapper<T1 : Entity, T2 : Dto> : ObjectMapper<T1, T2>

interface DtoToEntityObjectMapper<T1 : Dto, T2 : Entity> : ObjectMapper<T1, T2>

interface DomainObjectMapper<T1 : Dto, T2 : Entity> {
    fun toEntity(obj: T1): T2
    fun toDto(obj: T2): T1
}

class DefaultDomainObjectMapper<T1 : Dto, T2 : Entity>(
    private val dtoToEntityObjectMapper: DtoToEntityObjectMapper<T1, T2>,
    private val entityToDtoObjectMapper: EntityToDtoObjectMapper<T2, T1>
) : DomainObjectMapper<T1, T2> {

    override fun toEntity(obj: T1): T2 {
        return dtoToEntityObjectMapper.map(obj)
    }

    override fun toDto(obj: T2): T1 {
        return entityToDtoObjectMapper.map(obj)
    }
}
