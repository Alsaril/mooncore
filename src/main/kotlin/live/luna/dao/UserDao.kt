package live.luna.dao

import live.luna.entity.User

interface UserDao : CommonDao<User> {
    fun getByEmail(email: String): User?
}