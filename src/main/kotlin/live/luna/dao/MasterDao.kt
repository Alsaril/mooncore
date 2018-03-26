package live.luna.dao

import live.luna.entity.Master

interface MasterDao : CommonDao<Master> {
    fun getList(limit: Int, offset: Int): List<Master>

    fun getInArea(limit: Int,
                  offset: Int,
                  prevLat1: Double?,
                  prevLon1: Double?,
                  prevLat2: Double?,
                  prevLon2: Double?,
                  lat1: Double,
                  lon1: Double,
                  lat2: Double,
                  lon2: Double): List<Master>
}
