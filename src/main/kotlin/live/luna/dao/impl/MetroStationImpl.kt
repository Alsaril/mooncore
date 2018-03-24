package live.luna.dao.impl

import live.luna.dao.MetroStationDao
import live.luna.entity.MetroStation
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class MetroStationImpl : MetroStationDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun getById(id: Long): MetroStation? {
        return em.find(MetroStation::class.java, id)
    }

    override fun getByName(name: String): MetroStation? {
        val query = em.criteriaBuilder.createQuery(MetroStation::class.java)

        val root = query.from(MetroStation::class.java)
        query.select(root)
        query.where(em.criteriaBuilder.equal(root.get<MetroStation>("name"), name))

        return em.createQuery(query)
                .resultList
                .takeIf { it.isNotEmpty() }
                ?.let { return it[0] }
    }
}