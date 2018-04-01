package live.luna.service.impl

import live.luna.dao.impl.SeanceDaoImpl
import live.luna.entity.Seance
import live.luna.service.SeanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SeanceServiceImpl : SeanceService {

    @Autowired
    private lateinit var seanceDao: SeanceDaoImpl

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
}