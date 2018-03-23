package live.luna.dao

import live.luna.entity.MetroLine

interface MetroLineDao {
    fun getById(id: Long): MetroLine?
    fun getByName(name: String): MetroLine?
}