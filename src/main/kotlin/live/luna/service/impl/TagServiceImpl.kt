package live.luna.service.impl

import live.luna.dao.TagDao
import live.luna.entity.Tag
import live.luna.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagServiceImpl : TagService {

    @Autowired
    private lateinit var tagDao: TagDao

    override fun insert(tag: Tag) {
        tagDao.insert(tag)
    }

    override fun update(tag: Tag) {
        tagDao.update(tag)
    }

    override fun delete(tag: Tag) {
        tagDao.delete(tag)
    }

    override fun getById(id: Long): Tag? {
        return tagDao.getById(id)
    }
}