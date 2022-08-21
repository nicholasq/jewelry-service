package xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.data

import xyz.nicholasq.jewelryservice.infrastructure.data.Entity

data class PipelineEntity(
    override var id: String?,
    val name: String?,
    val creationDate: String?,
    val lastUpdateDate: String?,
    val description: String?,
) : Entity(id)
