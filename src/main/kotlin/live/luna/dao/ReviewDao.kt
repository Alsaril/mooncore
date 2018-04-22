package live.luna.dao

import live.luna.entity.Review
import live.luna.graphql.Limit

interface ReviewDao : CommonDao<Review> {
    fun getMasterReviews(masterId: Long, limit: Limit): List<Review>
}