package live.luna.dao.impl

import live.luna.dao.SignDao
import live.luna.entity.Sign
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class SignDaoImpl : SignDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Sign) {
        em.persist(entity)
    }

    override fun update(entity: Sign) {
        em.merge(entity)
    }

    override fun delete(entity: Sign) {
        em.remove(entity)
    }

    override fun getById(id: Long): Sign? {
        return em.find(Sign::class.java, id)
    }
}