package xyz.nicholasq.jewelryservice.infrastructure.data

import com.google.cloud.firestore.Firestore

interface DataSource<T> {
    fun get(): T
}

class FirestoreDataSource(private val firestore: Firestore) : DataSource<Firestore> {
    override fun get() = firestore
}
