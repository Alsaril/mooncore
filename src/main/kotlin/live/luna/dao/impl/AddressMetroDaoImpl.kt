package live.luna.dao.impl

import live.luna.dao.AddressMetroDao
import live.luna.entity.AddressMetro
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class AddressMetroDaoImpl : AddressMetroDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: AddressMetro) {
        em.persist(entity)
    }

    override fun update(entity: AddressMetro) {
        em.merge(entity)
    }

    override fun delete(entity: AddressMetro) {
        em.remove(entity)
    }

    override fun getById(id: Long): AddressMetro? {
        return em.find(AddressMetro::class.java, id)
    }
}