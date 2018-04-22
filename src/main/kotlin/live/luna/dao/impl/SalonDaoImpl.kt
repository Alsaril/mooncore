package live.luna.dao.impl

import live.luna.dao.SalonDao
import live.luna.entity.Address
import live.luna.entity.Salon
import live.luna.graphql.Area
import live.luna.graphql.Limit
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate


@Repository
@Transactional
class SalonDaoImpl : SalonDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Salon) {
        em.persist(entity)
    }

    override fun update(entity: Salon) {
        em.merge(entity)
    }

    override fun delete(entity: Salon) {
        em.remove(entity)
    }

    override fun getById(id: Long): Salon? {
        return em.find(Salon::class.java, id)
    }

    override fun getById(id: Long, serviceTypes: List<Long>): Salon? {
        val salon = getById(id) ?: return null
        if (serviceTypes.isEmpty()) {
            return salon
        }
        return Salon(
                id = salon.id,
                name = salon.name,
                address = salon.address,
                avatar = salon.avatar,
                masters = salon.masters.filter { it.supportAllServiceTypes(serviceTypes) }
        )
    }

    override fun getList(limit: Limit, area: Area?, prevArea: Area?, serviceTypes: List<Long>?): List<Salon> {
        if (limit.limit <= 0 || limit.offset < 0) {
            return emptyList()
        }

        if (area != null && area.from.lat > area.to.lat) {
            area.from = area.to.also { area.to = area.from }
            return getList(limit, area, prevArea, serviceTypes)
        }

        if (prevArea != null && prevArea.from.lat > prevArea.to.lat) {
            prevArea.from = prevArea.to.also { prevArea.to = prevArea.from }
            return getList(limit, area, prevArea, serviceTypes)
        }

        val criteriaQuery = em.criteriaBuilder.createQuery(Salon::class.java)
        val root = criteriaQuery.from(Salon::class.java)
        val predicates = mutableListOf<Predicate>()
        val cb = em.criteriaBuilder

        area?.let {
            val join = root.join<Salon, Address>("address")
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

        return if (serviceTypes?.isNotEmpty() == true) {
            val resultList = em.createQuery(
                    criteriaQuery.select(root).where(*predicates.toTypedArray())
            ).resultList

            val filteredByServices = resultList.filter {
                it.masters.any { it.supportAllServiceTypes(serviceTypes) }
            }

            filteredByServices //.subList(limit.offset, filteredByServices.size).take(limit.limit)
        } else {
            val typedQuery = em.createQuery(criteriaQuery.select(root).where(*predicates.toTypedArray()))
//            typedQuery.firstResult = limit.offset
//            typedQuery.maxResults = limit.limit
            typedQuery.resultList
        }
    }
}