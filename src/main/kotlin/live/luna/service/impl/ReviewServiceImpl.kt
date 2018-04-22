package live.luna.service.impl

import live.luna.dao.ReviewDao
import live.luna.entity.Review
import live.luna.graphql.Limit
import live.luna.graphql.UserContext
import live.luna.service.ClientService
import live.luna.service.ReviewService
import live.luna.service.SalonService
import live.luna.service.SeanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReviewServiceImpl : ReviewService {

    @Autowired
    private lateinit var reviewDao: ReviewDao

    @Autowired
    private lateinit var clientService: ClientService

    @Autowired
    private lateinit var seanceService: SeanceService

    @Autowired
    private lateinit var salonService: SalonService

    override fun insert(review: Review) {
        reviewDao.insert(review)
    }

    override fun update(review: Review) {
        reviewDao.update(review)
    }

    override fun delete(review: Review) {
        reviewDao.delete(review)
    }

    override fun getById(id: Long): Review? {
        return reviewDao.getById(id)
    }


    override fun addReview(seanceId: Long, stars: Int, message: String?, context: UserContext): Review? {
        context.user ?: return null

        val client = clientService.getByUserId(context.user.id) ?: return null
        val seance = seanceService.getById(seanceId) ?: return null

        if (seance.client != client) {
            return null
        }

        val review = Review(
                client = client,
                seance = seance,
                stars = stars,
                message = message,
                date = Date()
        )

        reviewDao.insert(review)
        return review
    }

    override fun getMasterReviews(masterId: Long, limit: Limit): List<Review> {
        return reviewDao.getMasterReviews(masterId, limit)
    }

    override fun getSalonReviews(salonId: Long, limit: Limit): List<Review> {
        val salon = salonService.getById(salonId, null) ?: return emptyList()

        val totalReviews = ArrayList<Review>()
        salon.masters.forEach {
            totalReviews.addAll(getMasterReviews(it.id, limit))
        }

        return totalReviews
                .sortedByDescending { it.date }
                .drop(limit.offset)
                .take(limit.limit)
    }
}