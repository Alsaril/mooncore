package live.luna.service.impl

import live.luna.dao.SeanceDao
import live.luna.entity.Client
import live.luna.entity.Seance
import live.luna.service.SeanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

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

    override fun getClientSeances(client: Client): List<Seance> {
        return seanceDao.getClientSeances(client)
    }

    override fun getMasterSeancesInRange(masterId: Long, startTime: Date, endTime: Date): List<Seance> {
        return seanceDao.getMasterSeancesInRange(masterId, startTime, endTime)
    }
}