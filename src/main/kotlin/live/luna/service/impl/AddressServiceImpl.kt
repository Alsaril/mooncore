package live.luna.service.impl

import live.luna.dao.AddressDao
import live.luna.entity.Address
import live.luna.service.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddressServiceImpl : AddressService {

    @Autowired
    private lateinit var addressDao: AddressDao

    override fun save(address: Address) {
        addressDao.save(address)
    }

    override fun update(address: Address) {
        addressDao.update(address)
    }

    override fun delete(address: Address) {
        addressDao.delete(address)
    }

    override fun getById(id: Long): Address {
        return addressDao.getById(id)
    }
}