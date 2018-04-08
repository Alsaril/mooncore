package live.luna.dao.impl

import live.luna.dao.SeanceDao
import live.luna.entity.Client
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

    override fun getForClient(client: Client): List<Seance> {
        val query = em.criteriaBuilder.createQuery(Seance::class.java)
        val root = query.from(Seance::class.java)
        query
                .select(root)
                .where(em.criteriaBuilder.equal(root.get<Seance>("client"), client))

        return em.createQuery(query).resultList
    }
}