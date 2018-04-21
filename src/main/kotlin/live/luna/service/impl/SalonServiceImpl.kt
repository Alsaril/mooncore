package live.luna.service.impl

import live.luna.dao.SalonDao
import live.luna.entity.Salon
import live.luna.graphql.Area
import live.luna.graphql.Limit
import live.luna.service.SalonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SalonServiceImpl : SalonService {

    @Autowired
    private lateinit var salonDao: SalonDao

    override fun insert(salon: Salon) {
        salonDao.insert(salon)
    }

    override fun update(salon: Salon) {
        salonDao.update(salon)
    }

    override fun delete(salon: Salon) {
        salonDao.delete(salon)
    }

    override fun getById(id: Long): Salon? {
        return salonDao.getById(id)
    }

    override fun getList(limit: Limit, area: Area?, prevArea: Area?, serviceTypes: List<Long>?): List<Salon> {
        return salonDao.getList(limit, area, prevArea, serviceTypes)
    }
}