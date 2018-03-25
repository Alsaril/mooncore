package live.luna.service.impl

import live.luna.dao.AddressMetroDao
import live.luna.entity.AddressMetro
import live.luna.service.AddressMetroService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddressMetroServiceImpl : AddressMetroService {

    @Autowired
    private lateinit var addressMetroDao: AddressMetroDao

    override fun insert(addressMetro: AddressMetro) {
        addressMetroDao.insert(addressMetro)
    }

    override fun update(addressMetro: AddressMetro) {
        addressMetroDao.update(addressMetro)
    }

    override fun delete(addressMetro: AddressMetro) {
        addressMetroDao.delete(addressMetro)
    }

    override fun getById(id: Long): AddressMetro? {
        return addressMetroDao.getById(id)
    }
}