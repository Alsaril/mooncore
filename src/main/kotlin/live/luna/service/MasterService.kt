package live.luna.service

import live.luna.entity.Master
import live.luna.graphql.Area
import live.luna.graphql.Limit
import live.luna.graphql.UserContext

interface MasterService {
    fun insert(master: Master)
    fun update(master: Master)
    fun delete(master: Master)
    fun getById(id: Long): Master?
    fun getByUserId(userId: Long): Master?
    fun getList(limit: Limit, area: Area?, prevArea: Area?, serviceTypes: List<Long>?): List<Master>
    fun updateMaster(master: Master, context: UserContext): Master?
}
