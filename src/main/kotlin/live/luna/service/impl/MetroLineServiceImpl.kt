package live.luna.service.impl

import live.luna.dao.MetroLineDao
import live.luna.entity.MetroLine
import live.luna.service.MetroLineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MetroLineServiceImpl : MetroLineService {

    @Autowired
    private lateinit var metroLineDao: MetroLineDao

    override fun getById(id: Long): MetroLine? {
        return metroLineDao.getById(id)
    }

    override fun getByName(name: String): MetroLine? {
        return metroLineDao.getByName(name)
    }
}