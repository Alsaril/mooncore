package live.luna.service

import live.luna.entity.Service

interface ServiceService {
    fun insert(service: Service)
    fun update(service: Service)
    fun delete(service: Service)
    fun getById(id: Long): Service?
}