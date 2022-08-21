package xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.config

import com.google.cloud.firestore.Firestore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.api.Pipeline
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.api.PipelineController
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.data.PipelineEntity
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.data.PipelineRepository
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.data.PipelineRepositoryImpl
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.mapper.EntityToPipelineMapper
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.mapper.PipelineToEntityMapper
import xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.service.PipelineService
import xyz.nicholasq.jewelryservice.infrastructure.api.BaseCrudController
import xyz.nicholasq.jewelryservice.infrastructure.api.CrudController
import xyz.nicholasq.jewelryservice.infrastructure.config.CrudConfig
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncCrudRepository
import xyz.nicholasq.jewelryservice.infrastructure.data.DataSource
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DefaultDomainObjectMapper
import xyz.nicholasq.jewelryservice.infrastructure.mapper.DomainObjectMapper
import xyz.nicholasq.jewelryservice.infrastructure.service.BaseCrudService
import xyz.nicholasq.jewelryservice.infrastructure.service.CrudService

@Configuration
class PipelineCrudConfig : CrudConfig<Pipeline, PipelineEntity, String> {

    @Value("\${pipeline.firestore.collection}")
    lateinit var pipelineCollection: String

    @Autowired
    lateinit var dataSource: DataSource<Firestore>

    @Bean("defaultPipelineController")
    override fun crudController(): CrudController<Pipeline, String> {
        return BaseCrudController(crudService(), LoggerFactory.getLogger(PipelineController::class.java))
    }

    @Bean("pipelineCrudService")
    override fun crudService(): CrudService<Pipeline, String> {
        return BaseCrudService(
            domainObjectMapper(),
            crudRepository(),
            LoggerFactory.getLogger(PipelineService::class.java)
        )
    }

    @Bean("pipelineCrudRepository")
    override fun crudRepository(): AsyncCrudRepository<PipelineEntity, String> {
        return PipelineRepositoryImpl(
            dataSource.get(),
            pipelineCollection,
            LoggerFactory.getLogger(PipelineRepository::class.java)
        )
    }

    @Bean("pipelineDomainObjectMapper")
    override fun domainObjectMapper(): DomainObjectMapper<Pipeline, PipelineEntity> {
        return DefaultDomainObjectMapper(PipelineToEntityMapper(), EntityToPipelineMapper())
    }
}

