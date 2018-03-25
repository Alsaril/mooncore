package live.luna.dao

interface CommonDao<T> {
    fun insert(entity: T)
    fun update(entity: T)
    fun delete(entity: T)
    fun getById(id: Long): T?
}