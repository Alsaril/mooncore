package live.luna.service.impl

import live.luna.dao.ScheduleDao
import live.luna.entity.Schedule
import live.luna.service.ScheduleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScheduleServiceImpl : ScheduleService {

    @Autowired
    private lateinit var scheduleDao: ScheduleDao

    override fun insert(schedule: Schedule) {
        scheduleDao.insert(schedule)
    }

    override fun update(schedule: Schedule) {
        scheduleDao.update(schedule)
    }

    override fun delete(schedule: Schedule) {
        scheduleDao.delete(schedule)
    }

    override fun getById(id: Long): Schedule? {
        return scheduleDao.getById(id)
    }
}