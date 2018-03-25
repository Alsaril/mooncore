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
}