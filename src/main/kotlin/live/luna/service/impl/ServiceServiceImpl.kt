package live.luna.service.impl

import live.luna.dao.ServiceDao
import live.luna.entity.Material
import live.luna.entity.Photo
import live.luna.graphql.UserContext
import live.luna.service.MasterService
import live.luna.service.ServiceService
import live.luna.service.ServiceTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ServiceServiceImpl : ServiceService {

    @Autowired
    private lateinit var serviceDao: ServiceDao

    @Autowired
    private lateinit var masterService: MasterService

    @Autowired
    private lateinit var serviceTypeService: ServiceTypeService

    override fun insert(service: live.luna.entity.Service) {
        serviceDao.insert(service)
    }

    override fun update(service: live.luna.entity.Service) {
        serviceDao.update(service)
    }

    override fun delete(service: live.luna.entity.Service) {
        serviceDao.delete(service)
    }

    override fun getById(id: Long): live.luna.entity.Service? {
        return serviceDao.getById(id)
    }

    override fun addService(context: UserContext, typeId: Long, price: BigDecimal, description: String,
                            duration: Int, materials: List<Material>?, photos: List<Photo>?): live.luna.entity.Service? {
        if (context.user == null) {
            return null;
        }

        val master = masterService.getByUserId(context.user.id) ?: return null // wtf?
        val serviceType = serviceTypeService.getById(typeId) ?: return null

        val service = live.luna.entity.Service(
                master = master,
                type = serviceType,
                price = price,
                description = description,
                duration = duration,
                materials = materials ?: emptyList(),
                photos = photos ?: emptyList()
        )
        insert(service)
        return service
    }

    override fun removeService(serviceId: Long, context: UserContext): live.luna.entity.Service? {
        if (context.user == null) {
            return null
        }

        val master = masterService.getByUserId(context.user.id) ?: return null // wtf?
        val service = getById(serviceId) ?: return null

        if (service.master.id != master.id) {
            return null
        }

        delete(service)
        return service
    }
}