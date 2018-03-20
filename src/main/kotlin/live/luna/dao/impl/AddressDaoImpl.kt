package live.luna.dao.impl

import live.luna.dao.AddressDao
import live.luna.entity.Address
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
@Transactional
class AddressDaoImpl : AddressDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun insert(entity: Address) {
        em.persist(entity)
    }

    override fun update(entity: Address) {
        em.merge(entity)
    }

    override fun delete(entity: Address) {
        em.remove(entity)
    }

    override fun getById(id: Long): Address? {
        return em.find(Address::class.java, id)
    }
}