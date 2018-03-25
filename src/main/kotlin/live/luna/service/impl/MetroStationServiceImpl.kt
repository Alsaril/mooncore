package live.luna.service.impl

import live.luna.dao.MetroStationDao
import live.luna.entity.MetroStation
import live.luna.service.MetroStationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MetroStationServiceImpl : MetroStationService {

    @Autowired
    private lateinit var metroStationDao: MetroStationDao

    override fun getById(id: Long): MetroStation? {
        return metroStationDao.getById(id)
    }

    override fun getByName(name: String): MetroStation? {
        return metroStationDao.getByName(name)
    }
}