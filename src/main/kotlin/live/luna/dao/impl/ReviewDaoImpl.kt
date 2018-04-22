package live.luna.dao.impl

import live.luna.dao.ReviewDao
import live.luna.entity.Master
import live.luna.entity.Review
import live.luna.entity.Seance
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate


@Repository
@Transactional
class ReviewDaoImpl : ReviewDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Review) {
        return em.persist(entity)
    }

    override fun update(entity: Review) {
        em.merge(entity)
    }

    override fun delete(entity: Review) {
        em.remove(entity)
    }

    override fun getById(id: Long): Review? {
        return em.find(Review::class.java, id)
    }

    override fun getMasterReviews(masterId: Long): List<Review> {
        val criteriaQuery = em.criteriaBuilder.createQuery(Review::class.java)
        val root = criteriaQuery.from(Review::class.java)
        val predicates = mutableListOf<Predicate>()
        val cb = em.criteriaBuilder

        predicates.add(cb.equal(root.get<Seance>("seance").get<Master>("master").get<Long>("id"), masterId))

        return em.createQuery(
                criteriaQuery
                        .select(root)
                        .where(*predicates.toTypedArray())
                        .orderBy(cb.desc(root.get<Date>("date")))
        ).resultList
    }
}