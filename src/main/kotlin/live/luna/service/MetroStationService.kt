package live.luna.service

import live.luna.entity.MetroStation

interface MetroStationService {
    fun getById(id: Long): MetroStation?
    fun getByName(name: String): MetroStation?
}