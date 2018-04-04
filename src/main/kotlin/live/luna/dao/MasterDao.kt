package live.luna.dao

import live.luna.entity.Master
import live.luna.graphql.Area
import live.luna.graphql.Limit

interface MasterDao : CommonDao<Master> {
    fun getByUserId(userId: Long): Master?
    fun getList(limit: Limit, area: Area?, prevArea: Area?, serviceTypes: List<Long>?): List<Master>
}
