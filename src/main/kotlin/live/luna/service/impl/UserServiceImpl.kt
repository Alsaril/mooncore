package live.luna.service.impl

import live.luna.dao.UserDao
import live.luna.entity.User
import live.luna.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userDao: UserDao

    override fun insert(user: User) {
        userDao.insert(user)
    }

    override fun update(user: User) {
        userDao.update(user)
    }

    override fun delete(user: User) {
        userDao.delete(user)
    }

    override fun getById(id: Long): User? {
        return userDao.getById(id)
    }
}