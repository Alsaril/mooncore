package live.luna.service

import live.luna.entity.Schedule

interface ScheduleService {
    fun insert(schedule: Schedule)
    fun update(schedule: Schedule)
    fun delete(schedule: Schedule)
    fun getById(id: Long): Schedule?
}