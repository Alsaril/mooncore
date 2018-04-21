package live.luna.dao.impl

import live.luna.dao.SeanceDao
import live.luna.entity.Client
import live.luna.entity.Master
import live.luna.entity.Seance
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate


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

    override fun getClientSeances(client: Client): List<Seance> {
        val query = em.criteriaBuilder.createQuery(Seance::class.java)
        val root = query.from(Seance::class.java)
        query
                .select(root)
                .where(em.criteriaBuilder.equal(root.get<Seance>("client"), client))

        return em.createQuery(query).resultList
    }

    override fun getMasterSeancesInRange(masterId: Long, startTime: Date, endTime: Date): List<Seance> {
        val criteriaQuery = em.criteriaBuilder.createQuery(Seance::class.java)
        val root = criteriaQuery.from(Seance::class.java)
        val predicates = mutableListOf<Predicate>()
        val cb = em.criteriaBuilder

        predicates.add(cb.equal(root.get<Master>("master").get<Long>("id"), masterId))
        predicates.add(cb.greaterThanOrEqualTo(root.get<Date>("endTime"), startTime))
        predicates.add(cb.lessThanOrEqualTo(root.get<Date>("startTime"), endTime))

        return em.createQuery(
                criteriaQuery
                        .select(root)
                        .where(*predicates.toTypedArray())
                        .orderBy(cb.asc(root.get<Date>("startTime")))
        ).resultList
    }
}