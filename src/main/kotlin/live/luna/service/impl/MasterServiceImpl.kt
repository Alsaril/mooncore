package live.luna.service.impl

import live.luna.dao.MasterDao
import live.luna.entity.Master
import live.luna.service.MasterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MasterServiceImpl : MasterService {

    @Autowired
    private lateinit var masterDao: MasterDao

    override fun insert(master: Master) {
        masterDao.insert(master)
    }

    override fun update(master: Master) {
        masterDao.update(master)
    }

    override fun delete(master: Master) {
        masterDao.delete(master)
    }

    override fun getById(id: Long): Master? {
        return masterDao.getById(id)
    }

    override fun getList(limit: Int, offset: Int): List<Master> {
        return masterDao.getList(limit, offset)
    }

    override fun getInArea(limit: Int,
                           offset: Int,
                           prevLat1: Double?,
                           prevLon1: Double?,
                           prevLat2: Double?,
                           prevLon2: Double?,
                           lat1: Double,
                           lon1: Double,
                           lat2: Double,
                           lon2: Double): List<Master> {
        return masterDao.getInArea(limit, offset, prevLat1, prevLon1, prevLat2, prevLon2, lat1, lon1, lat2, lon2)
    }
}