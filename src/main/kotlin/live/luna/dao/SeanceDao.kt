package live.luna.dao

import live.luna.entity.Client
import live.luna.entity.Seance
import java.util.*

interface SeanceDao : CommonDao<Seance> {
    fun getClientSeances(client: Client): List<Seance>
    fun getMasterSeancesInRange(masterId: Long, startTime: Date, endTime: Date): List<Seance>
}