package live.luna.service.impl

import live.luna.dao.PhotoDao
import live.luna.entity.Photo
import live.luna.service.PhotoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PhotoServiceImpl : PhotoService {

    @Autowired
    private lateinit var photoDao: PhotoDao

    override fun insert(photo: Photo) {
        photoDao.insert(photo)
    }

    override fun update(photo: Photo) {
        photoDao.update(photo)
    }

    override fun delete(photo: Photo) {
        photoDao.delete(photo)
    }

    override fun getById(id: Long): Photo? {
        return photoDao.getById(id)
    }
}