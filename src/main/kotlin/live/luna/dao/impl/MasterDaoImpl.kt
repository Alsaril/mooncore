package live.luna.dao.impl

import live.luna.dao.MasterDao
import live.luna.entity.Address
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

    override fun getInArea(limit: Int, offset: Int,
                           x1: Double, y1: Double, x2: Double, y2: Double): List<Master> {
        if (x1 == x2 || y1 == y2) {
            return emptyList()
        }

        if (x1 > x2) {
            return getInArea(limit, offset, x2, y2, x1, y1)
        }

        val criteriaQuery = em.criteriaBuilder.createQuery(Master::class.java)
        val root = criteriaQuery.from(Master::class.java)
        val join = root.join<Master, Address>("address")

        val select = criteriaQuery
                .select(root)
                .where(
                        em.criteriaBuilder.ge(join.get("lat"), x1),
                        em.criteriaBuilder.le(join.get("lat"), x2),
                        em.criteriaBuilder.ge(join.get("lon"), Math.min(y1, y2)),
                        em.criteriaBuilder.le(join.get("lon"), Math.max(y1, y2))
                )

        val typedQuery = em.createQuery(select)
        typedQuery.firstResult = offset
        typedQuery.maxResults = limit
        return typedQuery.resultList
    }
}