package xyz.nicholasq.jewelryservice.infrastructure.config

import xyz.nicholasq.jewelryservice.infrastructure.api.CrudController
import xyz.nicholasq.jewelryservice.infrastructure.api.Resource
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncCrudRepository
import xyz.nicholasq.jewelryservice.infrastructure.data.Entity
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DomainObjectMapper
import xyz.nicholasq.jewelryservice.infrastructure.service.CrudService

interface CrudConfig<T1 : Resource, T2 : Entity, ID> {
    fun crudController(): CrudController<T1, ID>
    fun crudService(): CrudService<T1, ID>
    fun crudRepository(): AsyncCrudRepository<T2, ID>
    fun domainObjectMapper(): DomainObjectMapper<T1, T2>
}
