package live.luna.dao.impl

import live.luna.dao.TagDao
import live.luna.entity.Tag
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class TagDaoImpl : TagDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Tag) {
        em.persist(entity)
    }

    override fun update(entity: Tag) {
        em.merge(entity)
    }

    override fun delete(entity: Tag) {
        em.remove(entity)
    }

    override fun getById(id: Long): Tag? {
        return em.find(Tag::class.java, id)
    }
}