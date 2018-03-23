package live.luna.service

import live.luna.entity.Client

interface ClientService {
    fun insert(client: Client)
    fun update(client: Client)
    fun delete(client: Client)
    fun getById(id: Long): Client?
}