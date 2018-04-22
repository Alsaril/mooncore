package live.luna.service

import live.luna.entity.Schedule
import live.luna.graphql.annotations.GraphQLField
import live.luna.graphql.annotations.GraphQLObject
import java.util.*

interface ScheduleService {
    fun insert(schedule: Schedule)
    fun update(schedule: Schedule)
    fun delete(schedule: Schedule)
    fun getById(id: Long): Schedule?

    /**
     * Возвращает расписание (рабочее время) мастера в данном промежутке
     */
    fun getMasterScheduleInRange(masterId: Long, startTime: Date, endTime: Date): List<Schedule>

    /**
     * Возвращает периоды времени, когда мастер может принять клиента
     * (на days дней вперед, начиная с текущего момента)
     */
    fun getMasterFreeTime(masterId: Long, days: Int): List<Period>

    @GraphQLObject
    class Period(
            @GraphQLField
            val from: Date,
            @GraphQLField
            val to: Date
    )
}