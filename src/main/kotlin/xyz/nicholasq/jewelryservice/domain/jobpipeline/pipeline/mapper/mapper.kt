package xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.mapper

import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.api.Pipeline
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.data.PipelineEntity
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DtoToEntityObjectMapper
import xyz.nicholasq.jewelryservice.infrastructure.mapper.EntityToDtoObjectMapper

class PipelineToEntityMapper : DtoToEntityObjectMapper<Pipeline, PipelineEntity> {
    override fun map(obj: Pipeline): PipelineEntity {
        return PipelineEntity(
            id = obj.id,
            name = obj.name,
            creationDate = obj.creationDate,
            lastUpdateDate = obj.lastUpdateDate,
            description = obj.description,
        )
    }
}

class EntityToPipelineMapper : EntityToDtoObjectMapper<PipelineEntity, Pipeline> {
    override fun map(obj: PipelineEntity): Pipeline {
        return Pipeline(
            id = obj.id,
            creationDate = obj.creationDate,
            lastUpdateDate = obj.lastUpdateDate,
            name = obj.name,
            description = obj.description,
        )
    }
}
