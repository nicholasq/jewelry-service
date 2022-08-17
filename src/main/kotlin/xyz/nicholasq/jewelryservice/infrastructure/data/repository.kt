package xyz.nicholasq.jewelryservice.infrastructure.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.slf4j.Logger
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface AsyncCrudRepository<T, ID> {
    suspend fun count(): Long
    suspend fun save(entity: T): T
    suspend fun saveAll(entity: Iterable<T>): T
    suspend fun findById(id: ID): T
    suspend fun findAll(): List<T>
    suspend fun delete(id: ID): Boolean
    suspend fun deleteAll(): Boolean
    suspend fun deleteAllById(id: ID): Boolean
}

abstract class AsyncFirestoreCrudRepository<T : Entity, ID>(
    firestore: Firestore,
    private val collection: String,
    private val logger: Logger // todo: log calls to db, probably on trace
) : AsyncCrudRepository<T, String> {

    private val db = firestore

    abstract fun entityClass(): Class<T>

    override suspend fun count(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun save(entity: T): T {
        logger.debug("save() - entity: $entity")
        // This is an initial save instead of an update.
        val newDoc = entity.id == null
        val item: ApiFuture<DocumentReference> = db.collection(collection).add(entity)
        val docRef = item.await { it }
        val docSnap = docRef.get().await { it }
        if (docSnap.exists() && docSnap.data != null) {
            val data = docSnap.data!!
            if (newDoc) {
                data["id"] = docRef.id
            }
            return toObject(entityClass(), data)
        } else {
            throw IOException("Document does not exist")
        }
    }

    override suspend fun saveAll(entity: Iterable<T>): T {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: String): T {
        logger.debug("findById() - '$id'")
        return db.collection(collection).document(id).toObject(entityClass())
    }

    // todo: wondering if the building of the list should be in a coroutine...?
    override suspend fun findAll(): List<T> {
        logger.debug("findAll()")
        val list = mutableListOf<T>()
        val querySnap = db.collection(collection).get().await { it }
        querySnap.forEach { doc ->
            if (doc.exists()) {
                list.add(toObject(entityClass(), doc.data!!))
            }
        }
        return list
    }

    override suspend fun delete(id: String): Boolean {
        return db.collection(collection).document(id).delete().await { true }
    }

    override suspend fun deleteAll(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllById(id: String): Boolean {
        TODO("Not yet implemented")
    }
}

private val objectMapper = jacksonObjectMapper()

private fun <T> toObject(clazz: Class<T>, map: MutableMap<String, Any>): T {
    return objectMapper.convertValue(map, clazz)
}

private suspend fun <T> DocumentReference.toObject(clazz: Class<T>): T {
    val docSnap: DocumentSnapshot = this.get().await { it }
    return docSnap.objectWithId(clazz)!!
}

private fun <T> DocumentSnapshot.objectWithId(clazz: Class<T>): T {
    val data = this.data
    if (this.exists() && data != null) {
        data["id"] = this.id
        return objectMapper.convertValue(data, clazz)
    } else {
        throw DocumentNotFoundException("Document '${this.id}' does not exist")
    }
}

private suspend fun <F : Any?, R : Any?> ApiFuture<F>.await(
    successHandler: (F) -> R,
): R {
    return suspendCoroutine { cont: Continuation<R> ->
        ApiFutures.addCallback(this, object : ApiFutureCallback<F> {
            override fun onFailure(t: Throwable?) {
                cont.resumeWithException(t ?: IOException("Unknown error when retrieving data from ApiFuture"))
            }

            override fun onSuccess(result: F) {
                cont.resume(successHandler(result))
            }
        }, Dispatchers.IO.asExecutor())
    }
}
