package live.luna.service

import live.luna.entity.Master

interface MasterService {
    fun insert(master: Master)
    fun update(master: Master)
    fun delete(master: Master)
    fun getById(id: Long): Master?
    fun getList(limit: Int, offset: Int): List<Master>
    fun getInArea(limit: Int, offset: Int,
                  prevX1: Double?, prevY1: Double?, prevX2: Double?, prevY2: Double?,
                  x1: Double, y1: Double, x2: Double, y2: Double): List<Master>
}