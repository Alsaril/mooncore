package live.luna.service

import live.luna.entity.Review
import live.luna.graphql.UserContext

interface ReviewService {
    fun insert(review: Review)
    fun update(review: Review)
    fun delete(review: Review)
    fun getById(id: Long): Review?
    fun addReview(masterId: Long, seanceId: Long, stars: Int, message: String?, context: UserContext): Review?
}