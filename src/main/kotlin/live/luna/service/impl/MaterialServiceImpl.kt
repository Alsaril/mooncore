package live.luna.service.impl

import live.luna.dao.MaterialDao
import live.luna.entity.Material
import live.luna.service.MaterialService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MaterialServiceImpl : MaterialService {

    @Autowired
    private lateinit var materialDao: MaterialDao

    override fun insert(material: Material) {
        materialDao.insert(material)
    }

    override fun update(material: Material) {
        materialDao.update(material)
    }

    override fun delete(material: Material) {
        materialDao.delete(material)
    }

    override fun getById(id: Long): Material? {
        return materialDao.getById(id)
    }
}