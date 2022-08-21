package xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.service

import xyz.nicholasq.jewelryservice.infrastructure.service.CrudService

interface PipelineService<T, ID> : CrudService<T, ID>
