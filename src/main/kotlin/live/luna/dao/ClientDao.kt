package live.luna.dao

import live.luna.entity.Client

interface ClientDao : CommonDao<Client> {
    fun getByUserId(userId: Long): Client?
}
