package live.luna.service.impl

import live.luna.dao.ClientDao
import live.luna.entity.Client
import live.luna.entity.Seance
import live.luna.graphql.UserContext
import live.luna.service.ClientService
import live.luna.service.MasterService
import live.luna.service.SeanceService
import live.luna.service.ServiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class ClientServiceImpl : ClientService {

    @Autowired
    private lateinit var clientDao: ClientDao

    @Autowired
    private lateinit var masterService: MasterService

    @Autowired
    private lateinit var serviceService: ServiceService

    @Autowired
    private lateinit var seanceService: SeanceService

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

    override fun makeAnAppointment(
            masterId: Long,
            servicesId: List<Long>,
            startTime: Date,
            endTime: Date,
            context: UserContext): Seance? {

        if (context.user == null || servicesId.isEmpty() || endTime.before(startTime)) {
            return null
        }

        val client = clientDao.getByUserId(context.user.id) ?: return null
        val master = masterService.getById(masterId) ?: return null

        val services: ArrayList<live.luna.entity.Service> = ArrayList()
        servicesId
                .forEach {
                    val service = serviceService.getById(it) ?: return null
                    services.add(service)
                }

        val seance = Seance(
                master = master,
                client = client,
                services = services,
                startTime = startTime,
                endTime = endTime
        )

        // TODO check availability not only by database trigger
        seanceService.insert(seance)
        return seance
    }

    override fun getMySeances(context: UserContext): List<Seance> {
        if (context.user == null) {
            return emptyList()
        }
        val client = clientDao.getByUserId(context.user.id) ?: return emptyList()
        return seanceService.getForClient(client)
    }
}