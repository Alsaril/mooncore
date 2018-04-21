package live.luna.service

import live.luna.entity.Client
import live.luna.entity.Seance
import live.luna.graphql.UserContext
import java.util.*

interface ClientService {
    fun insert(client: Client)
    fun update(client: Client)
    fun delete(client: Client)
    fun getById(id: Long): Client?
    fun makeAnAppointment(masterId: Long, servicesId: List<Long>,
                          startTime: Date, endTime: Date, context: UserContext): Seance?

    fun getClientSeances(context: UserContext): List<Seance>
}