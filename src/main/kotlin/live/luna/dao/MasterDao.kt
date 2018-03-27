package live.luna.dao

import live.luna.entity.Master
import live.luna.graphql.Area
import live.luna.graphql.Limit

interface MasterDao : CommonDao<Master> {
    fun getList(limit: Limit, area: Area?, prevArea: Area?): List<Master>
}
