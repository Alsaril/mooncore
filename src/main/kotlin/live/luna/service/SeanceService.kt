package live.luna.service

import live.luna.entity.Seance

interface SeanceService {
    fun insert(seance: Seance)
    fun update(seance: Seance)
    fun delete(seance: Seance)
    fun getById(id: Long): Seance?
}