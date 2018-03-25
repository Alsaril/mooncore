package live.luna.service.impl

import live.luna.dao.ClientDao
import live.luna.entity.Client
import live.luna.service.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl : ClientService {

    @Autowired
    private lateinit var clientDao: ClientDao

    override fun insert(client: Client) {
        clientDao.insert(client)
    }

    override fun update(client: Client) {
        clientDao.update(client)
    }

    override fun delete(client: Client) {
        clientDao.delete(client)
    }

    override fun getById(id: Long): Client? {
        return clientDao.getById(id)
    }
}