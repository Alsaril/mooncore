package live.luna.service

import live.luna.entity.Review

interface ReviewService {
    fun insert(review: Review)
    fun update(review: Review)
    fun delete(review: Review)
    fun getById(id: Long): Review?
}