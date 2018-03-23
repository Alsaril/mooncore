package live.luna.dao.impl

import live.luna.dao.MetroLineDao
import live.luna.entity.MetroLine
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class MetroLineImpl : MetroLineDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun getById(id: Long): MetroLine? {
        return em.find(MetroLine::class.java, id)
    }

    override fun getByName(name: String): MetroLine? {
        val query = em.criteriaBuilder.createQuery(MetroLine::class.java)

        val root = query.from(MetroLine::class.java)
        query.select(root)
        query.where(em.criteriaBuilder.equal(root.get<MetroLine>("name"), name))

        return em.createQuery(query)
                .resultList
                .takeIf { it.isNotEmpty() }
                ?.let { return it[0] }
    }
}