package live.luna.dao.impl

import live.luna.dao.MasterDao
import live.luna.entity.Address
import live.luna.entity.Master
import live.luna.graphql.Area
import live.luna.graphql.Limit
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate


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

    override fun getList(limit: Limit, area: Area?, prevArea: Area?): List<Master> {
        if (limit.limit <= 0 || limit.offset < 0) {
            return emptyList()
        }

        if (area != null && area.from.lat > area.to.lat) {
            area.from = area.to.also { area.to = area.from }
            return getList(limit, area, prevArea)
        }

        if (prevArea != null && prevArea.from.lat > prevArea.to.lat) {
            prevArea.from = prevArea.to.also { prevArea.to = prevArea.from }
            return getList(limit, area, prevArea)
        }

        val criteriaQuery = em.criteriaBuilder.createQuery(Master::class.java)
        val root = criteriaQuery.from(Master::class.java)
        val predicates = mutableListOf<Predicate>()
        val cb = em.criteriaBuilder

        area?.let {
            val join = root.join<Master, Address>("address")
            predicates.apply {
                add(cb.ge(join.get("lat"), area.from.lat))
                add(cb.le(join.get("lat"), area.to.lat))
                add(cb.ge(join.get("lon"), Math.min(area.from.lon, area.to.lon)))
                add(cb.le(join.get("lon"), Math.max(area.from.lon, area.to.lon)))
            }

            prevArea?.let {
                val l = listOf<Predicate>(
                        cb.lt(join.get("lat"), prevArea.from.lat),
                        cb.gt(join.get("lat"), prevArea.to.lat),
                        cb.lt(join.get("lon"), Math.min(prevArea.from.lon, prevArea.to.lon)),
                        cb.gt(join.get("lon"), Math.max(prevArea.from.lon, prevArea.to.lon))
                )
                predicates.add(cb.or(*l.toTypedArray()))
            }
        }

        val typedQuery = em.createQuery(criteriaQuery.select(root).where(*predicates.toTypedArray()))
        typedQuery.firstResult = limit.offset
        typedQuery.maxResults = limit.limit
        return typedQuery.resultList
    }
}