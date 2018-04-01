package live.luna.dao.impl

import live.luna.dao.ServiceTypeDao
import live.luna.entity.ServiceType
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class ServiceTypeDaoImpl : ServiceTypeDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: ServiceType) {
        em.persist(entity)
    }

    override fun update(entity: ServiceType) {
        em.merge(entity)
    }

    override fun delete(entity: ServiceType) {
        em.remove(entity)
    }

    override fun getById(id: Long): ServiceType? {
        return em.find(ServiceType::class.java, id)
    }

    override fun getByName(name: String): ServiceType? {
        val query = em.criteriaBuilder.createQuery(ServiceType::class.java)

        val root = query.from(ServiceType::class.java)
        query
                .select(root)
                .where(em.criteriaBuilder.equal(root.get<ServiceType>("name"), name))

        return em.createQuery(query)
                .resultList
                .takeIf { it.isNotEmpty() }
                ?.let { return it[0] }
    }
}