package live.luna.service

import live.luna.entity.Client
import live.luna.entity.Seance
import java.util.*

interface SeanceService {
    fun insert(seance: Seance)
    fun update(seance: Seance)
    fun delete(seance: Seance)
    fun getById(id: Long): Seance?

    // What about any limit here?
    fun getClientSeances(client: Client): List<Seance>

    /**
     * Возвращает сеансы мастера в заданном промежутке
     */
    fun getMasterSeancesInRange(masterId: Long, startTime: Date, endTime: Date): List<Seance>
}