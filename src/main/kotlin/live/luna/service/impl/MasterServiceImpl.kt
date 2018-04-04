package live.luna.service.impl

import live.luna.dao.MasterDao
import live.luna.entity.Master
import live.luna.graphql.Area
import live.luna.graphql.Limit
import live.luna.graphql.UserContext
import live.luna.service.MasterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MasterServiceImpl : MasterService {

    @Autowired
    private lateinit var masterDao: MasterDao

    override fun insert(master: Master) {
        masterDao.insert(master)
    }

    override fun update(master: Master) {
        masterDao.update(master)
    }

    override fun delete(master: Master) {
        masterDao.delete(master)
    }

    override fun getById(id: Long): Master? {
        return masterDao.getById(id)
    }

    override fun getByUserId(userId: Long): Master? {
        return masterDao.getByUserId(userId)
    }

    override fun getList(limit: Limit, area: Area?, prevArea: Area?,
                         serviceTypes: List<Long>?): List<Master> {
        return masterDao.getList(limit, area, prevArea, serviceTypes)
    }

    override fun updateMaster(master: Master, context: UserContext): Master? {
        if (context.user == null) {
            return null
        }

        val existing = getByUserId(context.user.id) ?: return null // wtf?
        val builder = Master.Builder.from(existing)

        master.name?.let {
            builder.setName(it)
        }
        master.address?.let {
            builder.setAddress(it)
        }
        master.avatar?.let {
            builder.setAvatar(it)
        }
        master.salon?.let {
            builder.setSalon(it)
        }

        val newMaster = builder.build()
        update(newMaster)
        return newMaster
    }
}