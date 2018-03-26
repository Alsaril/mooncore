package live.luna.dao

import live.luna.entity.Master

interface MasterDao : CommonDao<Master> {
    fun getList(limit: Int, offset: Int): List<Master>
    fun getByEmail(email: String): Master?

    fun getInArea(limit: Int, offset: Int,
                  prevX1: Double?, prevY1: Double?, prevX2: Double?, prevY2: Double?,
                  x1: Double, y1: Double, x2: Double, y2: Double): List<Master>
}
