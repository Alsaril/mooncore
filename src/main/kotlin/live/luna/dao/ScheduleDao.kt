package live.luna.dao

import live.luna.entity.Schedule
import java.util.*

interface ScheduleDao : CommonDao<Schedule> {
    fun getMasterScheduleInRange(masterId: Long, startTime: Date, endTime: Date): List<Schedule>
}
