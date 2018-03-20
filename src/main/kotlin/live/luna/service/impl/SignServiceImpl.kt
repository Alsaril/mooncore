package live.luna.service.impl

import live.luna.dao.SignDao
import live.luna.entity.Sign
import live.luna.service.SignService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SignServiceImpl : SignService {

    @Autowired
    private lateinit var signDao: SignDao

    override fun insert(sign: Sign) {
        signDao.insert(sign)
    }

    override fun update(sign: Sign) {
        signDao.update(sign)
    }

    override fun delete(sign: Sign) {
        signDao.delete(sign)
    }

    override fun getById(id: Long): Sign? {
        return signDao.getById(id)
    }
}