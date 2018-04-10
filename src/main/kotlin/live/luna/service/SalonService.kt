package live.luna.service

import live.luna.entity.Salon
import live.luna.graphql.Area
import live.luna.graphql.Limit

interface SalonService {
    fun insert(salon: Salon)
    fun update(salon: Salon)
    fun delete(salon: Salon)
    fun getById(id: Long): Salon?
    fun getList(limit: Limit, area: Area?, prevArea: Area?): List<Salon>
}