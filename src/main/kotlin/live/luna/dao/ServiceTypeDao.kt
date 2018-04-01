package live.luna.dao

import live.luna.entity.ServiceType

interface ServiceTypeDao : CommonDao<ServiceType> {
    fun getByName(name: String): ServiceType?
}
