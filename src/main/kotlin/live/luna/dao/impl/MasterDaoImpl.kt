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
    override fun getInArea(limit: Int,
                           offset: Int,
                           prevLat1: Double?,
                           prevLon1: Double?,
                           prevLat2: Double?,
                           prevLon2: Double?,
                           lat1: Double,
                           lon1: Double,
                           lat2: Double,
                           lon2: Double): List<Master> {

        if (lat1 == lat2 || lon1 == lon2) {
            return emptyList()
        }

        if (lat1 > lat2) {
            return getInArea(limit, offset, prevLat1, prevLon1, prevLat2, prevLon2, lat2, lon2, lat1, lon1)
        }

        if (prevLat1 != null && prevLat2 != null && prevLat1 > prevLat2) {
            return getInArea(limit, offset, prevLat2, prevLon2, prevLat1, prevLon1, lat1, lon1, lat2, lon2)
        }

        val criteriaQuery = em.criteriaBuilder.createQuery(Master::class.java)
        val root = criteriaQuery.from(Master::class.java)
        val join = root.join<Master, Address>("address")

        var select = criteriaQuery.select(root)

        if (prevLat1 != null && prevLat2 != null && prevLon1 != null && prevLon2 != null) {
            select = select.where(
                    em.criteriaBuilder.or(
                            em.criteriaBuilder.lt(join.get("lat"), prevLat1),
                            em.criteriaBuilder.gt(join.get("lat"), prevLat2),
                            em.criteriaBuilder.lt(join.get("lon"), Math.min(prevLon1, prevLon2)),
                            em.criteriaBuilder.gt(join.get("lon"), Math.max(prevLon1, prevLon2))
                    )
            )
        }

        select = select.where(
                em.criteriaBuilder.ge(join.get("lat"), lat1),
                em.criteriaBuilder.le(join.get("lat"), lat2),
                em.criteriaBuilder.ge(join.get("lon"), Math.min(lon1, lon2)),
                em.criteriaBuilder.le(join.get("lon"), Math.max(lon1, lon2))
        )

        val typedQuery = em.createQuery(select)
        typedQuery.firstResult = offset
        typedQuery.maxResults = limit
        return typedQuery.resultList
    }
}