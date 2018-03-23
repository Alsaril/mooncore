package live.luna.dao.impl

import live.luna.dao.PhotoDao
import live.luna.entity.Photo
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional
class PhotoDaoImpl : PhotoDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Photo) {
        em.persist(entity)
    }

    override fun update(entity: Photo) {
        em.merge(entity)
    }

    override fun delete(entity: Photo) {
        em.remove(entity)
    }

    override fun getById(id: Long): Photo? {
        return em.find(Photo::class.java, id)
    }
}