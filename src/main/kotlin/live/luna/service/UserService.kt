package live.luna.service

import live.luna.entity.User

interface UserService {
    fun insert(user: User)
    fun update(user: User)
    fun delete(user: User)
    fun getById(id: Long): User?
    fun getByEmail(email: String): User?
    fun createUser(email: String, password: String, name: String, role: Int): User?
    fun token(email: String, password: String): String?
}