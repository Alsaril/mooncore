package live.luna.service

import live.luna.entity.Master
import live.luna.graphql.Area
import live.luna.graphql.Limit

interface MasterService {
    fun insert(master: Master)
    fun update(master: Master)
    fun delete(master: Master)
    fun getById(id: Long): Master?
    fun getList(limit: Limit, area: Area?, prevArea: Area?): List<Master>
}
