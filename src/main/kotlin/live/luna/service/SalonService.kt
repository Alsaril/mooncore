package live.luna.service

import live.luna.entity.Salon

interface SalonService {
    fun insert(salon: Salon)
    fun update(salon: Salon)
    fun delete(salon: Salon)
    fun getById(id: Long): Salon?
}