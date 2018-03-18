package live.luna.dao.impl

import live.luna.dao.AddressDao
import live.luna.entity.Address
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
class AddressDaoImpl : AddressDao {

    @PersistenceContext
    private lateinit var em: EntityManager


    override fun save(address: Address) {
        em.merge(address)
    }

    override fun update(address: Address) {
        em.refresh(address);
    }

    override fun delete(address: Address) {
        em.remove(address)
    }

    override fun getById(id: Long): Address {
        return em.find(Address::class.java, id)
    }
}