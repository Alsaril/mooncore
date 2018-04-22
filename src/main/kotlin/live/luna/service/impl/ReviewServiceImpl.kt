package live.luna.service.impl

import live.luna.dao.ReviewDao
import live.luna.entity.Review
import live.luna.service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl : ReviewService {

    @Autowired
    private lateinit var reviewDao: ReviewDao

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
}