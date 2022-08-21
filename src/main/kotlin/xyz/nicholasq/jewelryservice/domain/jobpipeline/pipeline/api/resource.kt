package xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.api

import com.fasterxml.jackson.annotation.JsonProperty
import xyz.nicholasq.jewelryservice.infrastructure.api.Resource

data class Pipeline(
    @JsonProperty("id")
    override var id: String?,
    @JsonProperty("creation_date")
    var creationDate: String?,
    @JsonProperty("last_update_date")
    var lastUpdateDate: String?,
    @JsonProperty("name")
    var name: String?,
    @JsonProperty("description")
    var description: String?,
) : Resource(id)
