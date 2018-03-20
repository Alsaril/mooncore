package live.luna.service

import live.luna.entity.Sign

interface SignService {
    fun insert(sign: Sign)
    fun update(sign: Sign)
    fun delete(sign: Sign)
    fun getById(id: Long): Sign?
}