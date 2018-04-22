package live.luna.service

import live.luna.entity.Material
import live.luna.entity.Photo
import live.luna.entity.Service
import live.luna.graphql.UserContext
import java.math.BigDecimal

interface ServiceService {
    fun insert(service: Service)
    fun update(service: Service)
    fun delete(service: Service)
    fun getById(id: Long): Service?

    fun addService(
            context: UserContext,
            typeId: Long,
            price: BigDecimal,
            description: String,
            duration: Int,
            materials: List<Material>?,
            photos: List<Photo>?
    ): Service?

    fun removeService(serviceId: Long, context: UserContext): Service?
}