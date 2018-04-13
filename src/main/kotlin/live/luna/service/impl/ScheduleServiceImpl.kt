package live.luna.service.impl

import live.luna.dao.ScheduleDao
import live.luna.entity.Schedule
import live.luna.entity.Seance
import live.luna.service.ScheduleService
import live.luna.service.SeanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class ScheduleServiceImpl : ScheduleService {

    @Autowired
    private lateinit var scheduleDao: ScheduleDao

    @Autowired
    private lateinit var seanceService: SeanceService

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

    override fun getMasterScheduleInRange(masterId: Long, startTime: Date, endTime: Date): List<Schedule> {
        return scheduleDao.getMasterScheduleInRange(masterId, startTime, endTime)
    }

    override fun getMasterFreeTime(masterId: Long, days: Int): List<ScheduleService.Period> {
        val startTime = Date()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, days)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        val endTime = calendar.time

        val schedules = getMasterScheduleInRange(masterId, startTime, endTime).sortedBy { it.startTime }  // расписание мастера
        val seances = seanceService.getMasterSeancesInRange(masterId, startTime, endTime).sortedBy { it.startTime }  // его сеансы


        // осталось найти грубо говоря разность множеств
        val result = ArrayList<ScheduleService.Period>()

        schedules.forEach {
            result.addAll(getFreeParts(it, seances))
        }

        return result
    }

    /**
     * Если принять за schedule рабочий день мастера, то функция вернет периоды времени, в которые
     * мастер сегодня свободен и готов принять клиента
     */
    private fun getFreeParts(schedule: Schedule, seances: List<Seance>): List<ScheduleService.Period> {
        if (!schedule.endTime.after(schedule.startTime)) {
            return emptyList()
        }

        val possibleIntercects = seances
                .dropWhile { !schedule.startTime.before(it.endTime) }
                .dropLastWhile { !schedule.endTime.after(it.startTime) }

        if (possibleIntercects.isEmpty()) {
            return ArrayList<ScheduleService.Period>().apply {
                add(ScheduleService.Period(schedule.startTime, schedule.endTime))
            }
        }

        // Есть сеансы, и они пересекаются с schedule

        // true - ищем дальше либо конец сеанса либо конец окна. false - ищем начало сеанса (либо конец всего окна)
        var insideSeance: Boolean
        var seanceIndex = 0

        var start: Date
        var end: Date
        val result = ArrayList<ScheduleService.Period>()

        if (!schedule.startTime.before(possibleIntercects.first().startTime)) {
            // Сеанс начался сразу с начала окна или еще раньше (хммм, ну вдруг такое возможно)
            // Тогда начинаем поиск сразу с конца первого сеанса
            insideSeance = false
            start = possibleIntercects[seanceIndex++].endTime
            end = if (possibleIntercects.size > 1) {
                possibleIntercects[1].startTime   // в любом случае это раньше чем schedule.endTime ибо проверка выше
            } else {
                schedule.endTime
            }
            if (!end.after(start)) {    // сеанс занимает все окно полностью (и возможно больше)
                return emptyList()
            }


        } else {   // В начале смены у мастера есть свободное время попить чаек
            insideSeance = true
            start = schedule.startTime
            end = possibleIntercects[seanceIndex].startTime  // тут все просто
        }

        result.add(ScheduleService.Period(start, end)) // есть первый свободный отрезок

        if (end == schedule.endTime) {
            return result
        }

        do {
            start = end
            if (insideSeance) {
                end = if (schedule.endTime.after(possibleIntercects[seanceIndex].endTime)) {
                    possibleIntercects[seanceIndex].endTime
                } else {
                    schedule.endTime
                }
                seanceIndex++
                insideSeance = false
            } else {
                if (seanceIndex < possibleIntercects.size &&
                        possibleIntercects[seanceIndex].startTime.before(schedule.endTime)) {
                    end = possibleIntercects[seanceIndex].startTime
                    insideSeance = true
                } else {
                    end = schedule.endTime
                }
                if (start != end) {
                    result.add(ScheduleService.Period(start, end))
                }
            }
        } while (end != schedule.endTime)

        return result
    }
}