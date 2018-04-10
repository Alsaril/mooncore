package live.luna.dao.impl

import live.luna.dao.ScheduleDao
import live.luna.entity.Schedule
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


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
}