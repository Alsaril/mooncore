package live.luna.dao.impl

import live.luna.dao.AddressDao
import live.luna.entity.Address
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
class AddressDaoImpl : AddressDao {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    override fun save(address: Address): Address {
        return em.merge(address)
    }

    @Transactional
    override fun update(address: Address) {
        em.refresh(address)
    }

    @Transactional
    override fun delete(address: Address) {
        em.remove(address)
    }

    @Transactional
    override fun getById(id: Long): Address? {
        val a = em.find(Address::class.java, id)
        return a
    }
}