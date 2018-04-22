package live.luna.dao

import live.luna.entity.Review

interface ReviewDao : CommonDao<Review> {
    fun getMasterReviews(masterId: Long): List<Review>
}