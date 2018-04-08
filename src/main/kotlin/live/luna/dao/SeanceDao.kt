package live.luna.dao

import live.luna.entity.Client
import live.luna.entity.Seance

interface SeanceDao : CommonDao<Seance> {
    fun getForClient(client: Client): List<Seance>
}