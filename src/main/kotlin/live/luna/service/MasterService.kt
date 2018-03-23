package live.luna.service

import live.luna.entity.Master

interface MasterService {
    fun insert(master: Master)
    fun update(master: Master)
    fun delete(master: Master)
    fun getById(id: Long): Master?
}