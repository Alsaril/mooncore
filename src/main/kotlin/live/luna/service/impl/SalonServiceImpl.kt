package live.luna.service.impl

import live.luna.dao.SalonDao
import live.luna.entity.Review
import live.luna.entity.Salon
import live.luna.graphql.Area
import live.luna.graphql.Limit
import live.luna.service.ReviewService
import live.luna.service.SalonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SalonServiceImpl : SalonService {

    @Autowired
    private lateinit var salonDao: SalonDao

    @Autowired
    private lateinit var reviewService: ReviewService

    override fun insert(salon: Salon) {
        salonDao.insert(salon)
    }

    override fun update(salon: Salon) {
        salonDao.update(salon)
    }

    override fun delete(salon: Salon) {
        salonDao.delete(salon)
    }

    override fun getById(id: Long, serviceTypes: List<Long>?): Salon? {
        val salon = if (serviceTypes == null) {
            salonDao.getById(id) ?: return null
        } else {
            salonDao.getById(id, serviceTypes) ?: return null
        }

        val lastReviews = ArrayList<Review>()
        salon.masters.forEach {
            lastReviews.addAll(reviewService.getMasterReviews(it.id, Limit(0, 10)))
        }

        return Salon(
                id = salon.id,
                name = salon.name,
                address = salon.address,
                avatar = salon.avatar,
                masters = salon.masters,
                lastReviews = lastReviews.sortedByDescending { it.date }.take(10)
        )
    }

    override fun getList(limit: Limit, area: Area?, prevArea: Area?, serviceTypes: List<Long>?): List<Salon> {
        return salonDao.getList(limit, area, prevArea, serviceTypes)
    }
}