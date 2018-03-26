package live.luna.dao.impl

import live.luna.dao.MasterDao
import live.luna.entity.Master
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class MasterDaoImpl : MasterDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Master) {
        em.persist(entity)
    }

    override fun update(entity: Master) {
        em.merge(entity)
    }

    override fun delete(entity: Master) {
        em.remove(entity)
    }

    override fun getById(id: Long): Master? {
        return em.find(Master::class.java, id)
    }

    override fun getList(limit: Int, offset: Int): List<Master> {
        if (limit <= 0 || offset < 0) {
            return emptyList()
        }

        val criteriaQuery = em.criteriaBuilder.createQuery(Master::class.java)
        val from = criteriaQuery.from(Master::class.java)
        val select = criteriaQuery.select(from)

        val typedQuery = em.createQuery(select)
        typedQuery.firstResult = offset
        typedQuery.maxResults = limit

        return typedQuery.resultList
    }
}