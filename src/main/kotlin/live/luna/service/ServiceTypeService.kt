package live.luna.service

import live.luna.entity.ServiceType

interface ServiceTypeService {
    fun insert(serviceType: ServiceType)
    fun update(serviceType: ServiceType)
    fun delete(serviceType: ServiceType)
    fun getById(id: Long): ServiceType?
    fun getByName(name: String): ServiceType?
}