package live.luna.dao.impl

import live.luna.dao.MaterialDao
import live.luna.entity.Material
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class MaterialDaoImpl : MaterialDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Material) {
        em.persist(entity)
    }

    override fun update(entity: Material) {
        em.merge(entity)
    }

    override fun delete(entity: Material) {
        em.remove(entity)
    }

    override fun getById(id: Long): Material? {
        return em.find(Material::class.java, id)
    }
}