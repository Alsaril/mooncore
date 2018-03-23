package live.luna.service

import live.luna.entity.Photo

interface PhotoService {
    fun insert(photo: Photo)
    fun update(photo: Photo)
    fun delete(photo: Photo)
    fun getById(id: Long): Photo?
}