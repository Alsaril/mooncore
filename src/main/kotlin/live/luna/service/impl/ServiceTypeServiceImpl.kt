package live.luna.service.impl

import live.luna.dao.ServiceTypeDao
import live.luna.entity.ServiceType
import live.luna.service.ServiceTypeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ServiceTypeServiceImpl : ServiceTypeService {

    @Autowired
    private lateinit var serviceTypeDao: ServiceTypeDao

    override fun insert(serviceType: ServiceType) {
        serviceTypeDao.insert(serviceType)
    }

    override fun update(serviceType: ServiceType) {
        serviceTypeDao.update(serviceType)
    }

    override fun delete(serviceType: ServiceType) {
        serviceTypeDao.delete(serviceType)
    }

    override fun getById(id: Long): ServiceType? {
        return serviceTypeDao.getById(id)
    }

    override fun getByName(name: String): ServiceType? {
        return serviceTypeDao.getByName(name)
    }
}