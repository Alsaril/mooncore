package live.luna.service

import live.luna.entity.Tag

interface TagService {
    fun insert(tag: Tag)
    fun update(tag: Tag)
    fun delete(tag: Tag)
    fun getById(id: Long): Tag?
}