package repository

import util.VendingMachineException

abstract class BaseRepository<T> {

    protected val store = mutableMapOf<String, T>()

    protected abstract fun getId(entity: T): String

    open fun add(entity: T) {
        val id = getId(entity)
        if (existsById(id)) {
            throw VendingMachineException("Entity is either null or already exists in the system")
        }
        store[id] = entity
    }

    fun findById(id: String): T {
        return store[id] ?: throw VendingMachineException("Entity of Id: $id is either null or does not exist")
    }

    fun findAll(): Set<T> = store.values.toSet()

    fun removeById(id: String) {
        if (!existsById(id)) {
            throw VendingMachineException("Entity of Id: $id is either null or does not exist")
        }
        store.remove(id)
    }

    fun existsById(id: String): Boolean = store.containsKey(id)
}
