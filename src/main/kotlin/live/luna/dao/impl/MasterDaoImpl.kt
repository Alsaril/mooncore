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

    /**
     *  Getting list of masters in given area. Optionally you can pass previous area coordinates to exclude some data
     */
    override fun getInArea(limit: Int, offset: Int,
                           prevX1: Double?, prevY1: Double?, prevX2: Double?, prevY2: Double?,
                           x1: Double, y1: Double, x2: Double, y2: Double): List<Master> {

        if (x1 == x2 || y1 == y2) {
            return emptyList()
        }

        if (x1 > x2) {
            return getInArea(limit, offset, prevX1, prevY1, prevX2, prevY2, x2, y2, x1, y1)
        }

        if (prevX1 != null && prevX2 != null && prevX1 > prevX2) {
            return getInArea(limit, offset, prevX2, prevY2, prevX1, prevY1, x1, y1, x2, y2)
        }

        val criteriaQuery = em.criteriaBuilder.createQuery(Master::class.java)
        val root = criteriaQuery.from(Master::class.java)
        val join = root.join<Master, Address>("address")

        val select =
                if (prevX1 != null && prevX2 != null && prevY1 != null && prevY2 != null) {
                    criteriaQuery
                            .select(root)
                            .where(
                                    em.criteriaBuilder.or(
                                            em.criteriaBuilder.lt(join.get("lat"), prevX1),
                                            em.criteriaBuilder.gt(join.get("lat"), prevX2),
                                            em.criteriaBuilder.lt(join.get("lon"), Math.min(prevY1, prevY2)),
                                            em.criteriaBuilder.gt(join.get("lon"), Math.max(prevY1, prevY2))
                                    ),
                                    em.criteriaBuilder.ge(join.get("lat"), x1),
                                    em.criteriaBuilder.le(join.get("lat"), x2),
                                    em.criteriaBuilder.ge(join.get("lon"), Math.min(y1, y2)),
                                    em.criteriaBuilder.le(join.get("lon"), Math.max(y1, y2))
                            )
                } else {
                    criteriaQuery
                            .select(root)
                            .where(
                                    em.criteriaBuilder.ge(join.get("lat"), x1),
                                    em.criteriaBuilder.le(join.get("lat"), x2),
                                    em.criteriaBuilder.ge(join.get("lon"), Math.min(y1, y2)),
                                    em.criteriaBuilder.le(join.get("lon"), Math.max(y1, y2))
                            )
                }

        val typedQuery = em.createQuery(select)
        typedQuery.firstResult = offset
        typedQuery.maxResults = limit
        return typedQuery.resultList
    }
}