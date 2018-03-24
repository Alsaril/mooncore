package live.luna.dao

import live.luna.entity.MetroStation

interface MetroStationDao {
    fun getById(id: Long): MetroStation?
    fun getByName(name: String): MetroStation?
}