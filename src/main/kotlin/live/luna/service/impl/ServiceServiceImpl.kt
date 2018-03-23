package live.luna.service.impl

import live.luna.dao.ServiceDao
import live.luna.service.ServiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ServiceServiceImpl : ServiceService {

    @Autowired
    private lateinit var serviceDao: ServiceDao

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
}