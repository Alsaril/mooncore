package live.luna.dao

import live.luna.entity.Salon
import live.luna.graphql.Area
import live.luna.graphql.Limit

interface SalonDao : CommonDao<Salon> {
    fun getList(limit: Limit, area: Area?, prevArea: Area?, serviceTypes: List<Long>?): List<Salon>
}
