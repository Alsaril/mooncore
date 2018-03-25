package live.luna.dao

import live.luna.entity.Master

interface MasterDao : CommonDao<Master> {
    fun getList(limit: Int, offset: Int): List<Master>
    fun getInArea(limit: Int, offset: Int,
                  x1: Double, y1: Double, x2: Double, y2: Double): List<Master>
}
