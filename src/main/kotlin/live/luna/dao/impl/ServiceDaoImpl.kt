package live.luna.dao.impl

import live.luna.dao.ServiceDao
import live.luna.entity.Service
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class ServiceDaoImpl : ServiceDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Service) {
        em.persist(entity)
    }

    override fun update(entity: Service) {
        em.merge(entity)
    }

    override fun delete(entity: Service) {
        em.remove(entity)
    }

    override fun getById(id: Long): Service? {
        return em.find(Service::class.java, id)
    }
}