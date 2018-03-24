package live.luna.dao

import live.luna.entity.Master

interface MasterDao : CommonDao<Master> {
    fun getList(limit: Int, offset: Int): List<Master>
}
