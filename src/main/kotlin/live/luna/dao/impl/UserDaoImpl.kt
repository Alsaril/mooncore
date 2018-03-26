package live.luna.dao.impl

import live.luna.dao.UserDao
import live.luna.entity.User
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class UserDaoImpl : UserDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: User) {
        em.persist(entity)
    }

    override fun update(entity: User) {
        em.merge(entity)
    }

    override fun delete(entity: User) {
        em.remove(entity)
    }

    override fun getById(id: Long): User? {
        return em.find(User::class.java, id)
    }


    override fun getByEmail(email: String): User? {
        val cb = em.criteriaBuilder
        val query = cb.createQuery(User::class.java)

        val root = query.from(User::class.java)
        query.select(root)
                .where(cb.equal(root.get<String>("email"), email))

        return em.createQuery(query).singleResult
    }
}