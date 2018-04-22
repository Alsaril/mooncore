package live.luna.service

import live.luna.entity.Review
import live.luna.graphql.Limit
import live.luna.graphql.UserContext

interface ReviewService {
    fun insert(review: Review)
    fun update(review: Review)
    fun delete(review: Review)
    fun getById(id: Long): Review?
    fun addReview(seanceId: Long, stars: Int, message: String?, context: UserContext): Review?
    fun getMasterReviews(masterId: Long, limit: Limit): List<Review>
    fun getSalonReviews(salonId: Long, limit: Limit): List<Review>
}