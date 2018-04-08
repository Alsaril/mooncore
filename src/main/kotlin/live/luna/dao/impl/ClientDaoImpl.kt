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

    override fun getByUserId(userId: Long): Client? {
        val query = em.criteriaBuilder.createQuery(Client::class.java)
        val root = query.from(Client::class.java)
        query
                .select(root)
                .where(em.criteriaBuilder.equal(root.get<Client>("user").get<Long>("id"), userId))

        return em.createQuery(query)
                .resultList
                .takeIf { it.isNotEmpty() }
                ?.let { return it[0] }
    }
}