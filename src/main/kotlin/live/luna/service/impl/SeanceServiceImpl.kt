package live.luna.service.impl

import live.luna.dao.SeanceDao
import live.luna.entity.Client
import live.luna.entity.Seance
import live.luna.service.SeanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SeanceServiceImpl : SeanceService {

    @Autowired
    private lateinit var seanceDao: SeanceDao

    override fun insert(seance: Seance) {
        seanceDao.insert(seance)
    }

    override fun update(seance: Seance) {
        seanceDao.update(seance)
    }

    override fun delete(seance: Seance) {
        seanceDao.delete(seance)
    }

    override fun getById(id: Long): Seance? {
        return seanceDao.getById(id)
    }

    override fun getForClient(client: Client): List<Seance> {
        return seanceDao.getForClient(client)
    }
}