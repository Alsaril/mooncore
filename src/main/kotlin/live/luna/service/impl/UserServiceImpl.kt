package live.luna.service.impl

import live.luna.auth.AuthHelper
import live.luna.dao.UserDao
import live.luna.entity.Client
import live.luna.entity.Master
import live.luna.entity.User
import live.luna.service.ClientService
import live.luna.service.MasterService
import live.luna.service.UserService
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userDao: UserDao
    @Autowired
    private lateinit var masterService: MasterService
    @Autowired
    private lateinit var clientService: ClientService
    @Autowired
    protected lateinit var authHelper: AuthHelper

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

    override fun getByEmail(email: String): User? {
        return userDao.getByEmail(email)
    }

    @Transactional
    override fun createUser(email: String, password: String, name: String?, role: Int): User? {
        val user = User(email = email, password = BCrypt.hashpw(password, BCrypt.gensalt()), role = role)
        insert(user)
        when (role) {
            1 -> masterService.insert(Master(name = name, user = user))
            2 -> clientService.insert(Client(name = name, user = user))
            else -> throw IllegalArgumentException("Role must be 1 or 2")
        }
        return user
    }


    override fun token(email: String, password: String): String? {
        val user = getByEmail(email) ?: return null
        if (!BCrypt.checkpw(password, user.password)) {
            return null
        }
        return authHelper.generateToken(email)
    }
}