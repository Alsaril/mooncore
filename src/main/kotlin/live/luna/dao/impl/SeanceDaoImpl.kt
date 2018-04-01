package live.luna.dao.impl

import live.luna.dao.SeanceDao
import live.luna.entity.Seance
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class SeanceDaoImpl : SeanceDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Seance) {
        return em.persist(entity)
    }

    override fun update(entity: Seance) {
        em.merge(entity)
    }

    override fun delete(entity: Seance) {
        em.remove(entity)
    }

    override fun getById(id: Long): Seance? {
        return em.find(Seance::class.java, id)
    }
}