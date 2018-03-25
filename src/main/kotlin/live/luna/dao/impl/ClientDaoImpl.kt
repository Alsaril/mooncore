package live.luna.dao.impl

import live.luna.dao.ClientDao
import live.luna.entity.Client
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class ClientDaoImpl : ClientDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Client) {
        em.persist(entity)
    }

    override fun update(entity: Client) {
        em.merge(entity)
    }

    override fun delete(entity: Client) {
        em.remove(entity)
    }

    override fun getById(id: Long): Client? {
        return em.find(Client::class.java, id)
    }
}