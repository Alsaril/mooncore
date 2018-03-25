package live.luna.dao.impl

import live.luna.dao.SalonDao
import live.luna.entity.Salon
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class SalonDaoImpl : SalonDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Salon) {
        em.persist(entity)
    }

    override fun update(entity: Salon) {
        em.merge(entity)
    }

    override fun delete(entity: Salon) {
        em.remove(entity)
    }

    override fun getById(id: Long): Salon? {
        return em.find(Salon::class.java, id)
    }
}