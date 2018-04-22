package live.luna.dao.impl

import live.luna.dao.ReviewDao
import live.luna.entity.Review
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


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
}