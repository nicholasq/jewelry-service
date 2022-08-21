package xyz.nicholasq.jewelryservice.domain.jobpipeline.pipeline.data

import com.google.cloud.firestore.Firestore
import org.slf4j.Logger
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncCrudRepository
import xyz.nicholasq.jewelryservice.infrastructure.data.AsyncFirestoreCrudRepository

interface PipelineRepository<T, ID> : AsyncCrudRepository<T, ID>

class PipelineRepositoryImpl(firestore: Firestore, collection: String, logger: Logger) :
    AsyncFirestoreCrudRepository<PipelineEntity, String>(firestore, collection, logger) {
    override fun entityClass(): Class<PipelineEntity> {
        return PipelineEntity::class.java
    }
}
