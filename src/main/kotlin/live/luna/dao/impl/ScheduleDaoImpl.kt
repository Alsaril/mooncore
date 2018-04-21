package live.luna.dao.impl

import live.luna.dao.ScheduleDao
import live.luna.entity.Master
import live.luna.entity.Schedule
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate


@Repository
@Transactional
class ScheduleDaoImpl : ScheduleDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Schedule) {
        return em.persist(entity)
    }

    override fun update(entity: Schedule) {
        em.merge(entity)
    }

    override fun delete(entity: Schedule) {
        em.remove(entity)
    }

    override fun getById(id: Long): Schedule? {
        return em.find(Schedule::class.java, id)
    }

    override fun getMasterScheduleInRange(masterId: Long, startTime: Date, endTime: Date): List<Schedule> {
        val criteriaQuery = em.criteriaBuilder.createQuery(Schedule::class.java)
        val root = criteriaQuery.from(Schedule::class.java)
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